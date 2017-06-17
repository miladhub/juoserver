package net.sf.juoserver.protocol;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.MessageReader;
import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.model.City;
import net.sf.juoserver.model.Flag;
import net.sf.juoserver.model.UOItem;
import net.sf.juoserver.model.PlayingCharacter;
import net.sf.juoserver.model.ServerInfo;
import net.sf.juoserver.protocol.AbstractMessage;
import net.sf.juoserver.protocol.BadDecodableException;
import net.sf.juoserver.protocol.CharacterList;
import net.sf.juoserver.protocol.CharacterSelect;
import net.sf.juoserver.protocol.ClasspathMessageDecoderProvider;
import net.sf.juoserver.protocol.ClilocMessage;
import net.sf.juoserver.protocol.LoginRequest;
import net.sf.juoserver.protocol.LoginSeed;
import net.sf.juoserver.protocol.MegaClilocRequest;
import net.sf.juoserver.protocol.MegaClilocResponse;
import net.sf.juoserver.protocol.UOProtocolMessageReader;
import net.sf.juoserver.protocol.SelectServer;
import net.sf.juoserver.protocol.ServerConnect;
import net.sf.juoserver.protocol.ServerList;
import net.sf.juoserver.protocol.ServerLoginRequest;
import net.sf.juoserver.protocol.UpdatePlayer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

public class MessageReaderTest {
	private MessageReader reader, initializedReader;
	private byte[] ipBytes; // 4 bytes
	private byte[] loginBytes;
	private byte[] selectServerBytes;
	private byte[] serverLoginRequestBytes;
	private byte[] characterSelectBytes;
	
	@Before
	public void setUp() throws DecoderException {
		reader = new UOProtocolMessageReader();
		initializedReader = new UOProtocolMessageReader(new ClasspathMessageDecoderProvider(), true);
		
		ipBytes = Hex.decodeHex( "0A000064".toCharArray() );
		
		loginBytes = new byte[62];
		byte[] user = Arrays.copyOf("myuser".getBytes(Charset.forName(AbstractMessage.UTF8)), 30);
		byte[] psw = Arrays.copyOf("mypsw".getBytes(Charset.forName(AbstractMessage.UTF8)), 30);
		loginBytes[0] = Hex.decodeHex( "80".toCharArray() )[0];
		System.arraycopy(user, 0, loginBytes, 1, 30);
		System.arraycopy(psw, 0, loginBytes, 31, 30);
		
		selectServerBytes = new byte[3];
		ByteBuffer bb = ByteBuffer.wrap(selectServerBytes);
		bb.put( (byte) 0xA0 );
		bb.putShort((short) 42);
		
		serverLoginRequestBytes = new byte[65];
		bb = ByteBuffer.wrap(serverLoginRequestBytes);
		bb.put( (byte) 0x91 );
		bb.putInt(57);
		System.arraycopy(user, 0, serverLoginRequestBytes, 5, 30);
		System.arraycopy(psw, 0, serverLoginRequestBytes, 35, 30);
		
		characterSelectBytes = new byte[73];
		bb = ByteBuffer.wrap(characterSelectBytes);
		bb.put( (byte) 0x5D );
		bb.putInt(42);
		System.arraycopy(user, 0, characterSelectBytes, 5, 30);
		bb.position(bb.position() + 30);
		for (int i = 0; i < 33; i++) { bb.put((byte) 1); }
		bb.put((byte) 0);
		bb.put(ipBytes);
	}
	
	@Test
	public void buildReader() {
		ClasspathMessageDecoderProvider mdp = new ClasspathMessageDecoderProvider();
		UOProtocolMessageReader reader = new UOProtocolMessageReader();
		UOProtocolMessageReader initializedReader = new UOProtocolMessageReader(mdp, true);
		assertNotNull(reader);
		assertTrue(mdp.getDecoders().values().size() > 0);
		assertFalse(reader.seedSent);
		assertNotNull(initializedReader);
		assertTrue(initializedReader.seedSent);
	}
	
	@Test
	public void buildLoginSeed() throws DecoderException, UnknownHostException {
		LoginSeed msg = new LoginSeed( ipBytes );
		assertEquals("10.0.0.100", msg.getAddress().getHostAddress());
	}
	
	@Test
	public void readLoginSeed() throws DecoderException, UnknownHostException {
		assertEquals(Arrays.asList( new LoginSeed( ipBytes ) ), reader.readMessages(ipBytes));
	}
	
	@Test
	public void buildLoginRequest() throws DecoderException, UnknownHostException {
		LoginRequest msg = new LoginRequest( loginBytes );
		assertEquals("myuser", msg.getUser());
		assertEquals("mypsw", msg.getPassword());
	}
	
	@Test
	public void readLoginRequest() throws DecoderException, UnknownHostException {
		assertEquals(Arrays.asList( new LoginRequest( loginBytes ) ), initializedReader.readMessages(loginBytes));
	}
	
	@Test
	public void readLoginSeedAndRequestTogether() throws DecoderException, UnknownHostException {
		byte[] bytes = new byte[ipBytes.length + loginBytes.length];
		System.arraycopy(ipBytes, 0, bytes, 0, ipBytes.length);
		System.arraycopy(loginBytes, 0, bytes, ipBytes.length, loginBytes.length);
		List<Message> msgs = reader.readMessages(bytes);
		assertEquals(new LoginSeed( ipBytes ), msgs.get(0));
		assertEquals(new LoginRequest( loginBytes ), msgs.get(1));
	}
	
	@Test
	public void sendServerList() throws DecoderException, UnknownHostException {
		ServerList serverList = new ServerList(new ServerInfo("This server", InetAddress.getLocalHost()));
		byte[] bytes = serverList.encode().array();
		int length = 6 + 1 * (2 + 32 + 2 + 4);
		assertEquals(length, bytes.length);
		ByteBuffer bb = ByteBuffer.wrap( bytes );
		assertEquals((byte) ServerList.CODE, bb.get());
		assertEquals((short) length, bb.getShort());
		assertEquals(ServerList.UNKNOWN_BYTE, bb.get());
		assertEquals((short) 1, bb.getShort());
		assertEquals((short) 0, bb.getShort());
		byte[] stringBytes = new byte[ServerList.SERVER_NAME_SIZE];
		bb.get(stringBytes, 0, ServerList.SERVER_NAME_SIZE);
		assertEquals("This server",
				MessagesUtils.getString(stringBytes, 0, ServerList.SERVER_NAME_SIZE - 1));
		
		assertEquals((byte) 0, bb.get());
		assertEquals((byte) 0, bb.get());
		
		byte[] ipBytes = InetAddress.getLocalHost().getAddress();
		ArrayUtils.reverse( ipBytes );
		byte[] ipBytesActual = new byte[4];
		bb.get(ipBytesActual, 0, 4);
		assertTrue(Arrays.equals( ipBytesActual, Arrays.copyOf(ipBytes, 4) ));
	}

	@Test
	public void buildSelectServer() throws DecoderException, UnknownHostException {
		SelectServer msg = new SelectServer( selectServerBytes );
		assertEquals(42, msg.getServerNumber());
	}
	
	@Test
	public void readSelectServer() throws DecoderException, UnknownHostException {
		assertEquals(Arrays.asList( new SelectServer( selectServerBytes ) ), initializedReader.readMessages(selectServerBytes));
	}
	
	@Test
	public void sendServerConnect() throws UnknownHostException {
		ByteBuffer bytes = new ServerConnect(InetAddress.getLocalHost(), 7775, 42).encode();
		bytes.rewind();
		assertEquals((byte) ServerConnect.CODE, bytes.get());
		byte[] addressBytes = new byte[4];
		bytes.get(addressBytes, 0, 4);
		assertTrue(Arrays.equals( InetAddress.getLocalHost().getAddress(), addressBytes ));
		assertEquals(7775, bytes.getShort());
		assertEquals(42, bytes.getInt());
	}
	
	@Test
	public void buildServerLoginRequest() throws DecoderException, UnknownHostException {
		ServerLoginRequest msg = new ServerLoginRequest( serverLoginRequestBytes );
		assertEquals(57, msg.getAuthenticationKey());
		assertEquals("myuser", msg.getUser());
		assertEquals("mypsw", msg.getPassword());
	}
	
	@Test
	public void readServerLoginRequest() throws DecoderException, UnknownHostException {
		assertEquals(Arrays.asList( new ServerLoginRequest( serverLoginRequestBytes ) ),
				initializedReader.readMessages(serverLoginRequestBytes));
	}
	
	@Test
	public void sendCharacterList() {
		CharacterList serverList = new CharacterList(Arrays.asList(new PlayingCharacter("myuser", "mypsw")),
				Arrays.asList(new City("Moonglow", "somelocation")), 
				new Flag(0x14), new Flag(0x1A8)); // Limit characters & one character
		ByteBuffer bb = serverList.encode();
		bb.rewind();
		int length = 
			1 + // code 
			2 + // length
			1 + // pcs.size
			1 * (30 + 30) + // pc name, psw
			1 + // cities.size 
			1 * (1 + // city index 
					31 + 31) + // city name, location
			4; // flags
		
		assertEquals(length, bb.array().length);

		assertEquals((byte) CharacterList.CODE, bb.get());
		assertEquals((short) length, bb.getShort());
		assertEquals((byte) 1, bb.get());
		
		byte[] pcNameBytes = new byte[30];
		bb.get(pcNameBytes, 0, 30);
		assertEquals("myuser",
				MessagesUtils.getString(pcNameBytes, 0, 30 - 1));
		
		byte[] pcPswBytes = new byte[30];
		bb.get(pcPswBytes, 0, 30);
		assertEquals("mypsw",
				MessagesUtils.getString(pcPswBytes, 0, 30 - 1));
		
		assertEquals((byte) 1, bb.get());
		
		assertEquals((byte) 0, bb.get());
		
		byte[] cityNameBytes = new byte[31];
		bb.get(cityNameBytes, 0, 31);
		assertEquals("Moonglow",
				MessagesUtils.getString(cityNameBytes, 0, 31 - 1));
		
		byte[] cityLocationBytes = new byte[31];
		bb.get(cityLocationBytes, 0, 31);
		assertEquals("somelocation",
				MessagesUtils.getString(cityLocationBytes, 0, 31 - 1));
		
		assertEquals(0x14 | 0x1A8, bb.getInt());
	}
	
	@Test
	public void buildCharacterSelect() throws DecoderException, UnknownHostException {
		CharacterSelect msg = new CharacterSelect( characterSelectBytes );
		assertEquals((byte) 0, msg.getCharId());
		assertEquals("myuser", msg.getCharName());
	}
	
	@Test(expected=BadDecodableException.class)
	public void badDecodable() {
		ClasspathMessageDecoderProvider mdp = new ClasspathMessageDecoderProvider() {
			@Override
			protected void init() {
				super.configureFrom(MessageReaderTest.class);
			}
		};
		new UOProtocolMessageReader(mdp, true);
	}
	
	@SuppressWarnings("unused") // Used via reflection by ClasspathMessageDecoderProvider
	@Decodable(code=MyBadTestMessage.CODE)
	private class MyBadTestMessage extends AbstractMessage {
		public static final int CODE = 0x42;
		private static final long serialVersionUID = 1L;
		public MyBadTestMessage() {
			super(CODE, 42);
		}
		@Override
		public int hashCode() {
			return 0;
		}
		@Override
		public boolean equals(Object obj) {
			return false;
		}
	}
	
	@Test
	public void megaClilocRequest() throws DecoderException {
		MegaClilocRequest mcr = new MegaClilocRequest(
				Hex.decodeHex( "D6000700000001".toCharArray() ));
		assertEquals(7, mcr.getLength());
		assertEquals(Arrays.asList(1), mcr.getQuerySerials());
	}
	
	@Test
	public void megaClilocResponse() {
		Message mcr = MegaClilocResponse.createMobileMegaClilocResponse(TestingFactory.createTestMobile(42, "asder"));
		ByteBuffer bb = mcr.encode();
		bb.rewind();
		assertEquals((byte) 0xD6, bb.get());
		assertEquals(mcr.getLength(), bb.array().length);
		assertEquals(0x2B, mcr.getLength());
		assertEquals(0x2B, bb.getShort());
		assertTrue(MessagesUtils.getHexString( mcr.encode().array() ).endsWith(
				"20000900610073006400650072000900200000000000"));
	}
	
	@Test
	public void putUnicodeReverseString() {
		String str = " \tasder\t ";
		ByteBuffer bb = ByteBuffer.allocate(str.length() * 2);
		int pos = bb.position();
		MessagesUtils.putReverseUnicodeString(bb, str);
		assertEquals("200009006100730064006500720009002000", MessagesUtils.getHexString(bb.array()));
		assertEquals(pos + str.length() * 2, bb.position());
	}
	
	@Test
	public void putUnicodeReverseStringComposed() {
		String str = " \tasder\t ";
		ByteBuffer bb = ByteBuffer.allocate(2 + str.length() * 2);
		bb.putShort((short) (0x42 & 0xFF));
		MessagesUtils.putReverseUnicodeString(bb, str);
		assertEquals("0042200009006100730064006500720009002000", MessagesUtils.getHexString(bb.array()));
	}
	
	@Test
	public void putUnicodeString() {
		String str = " \tasder\t ";
		ByteBuffer bb = ByteBuffer.allocate(str.length() * 2);
		MessagesUtils.putUnicodeString(bb, str);
		assertEquals("002000090061007300640065007200090020", MessagesUtils.getHexString(bb.array()));
	}
	
	@Test
	public void mobileClilocMessage() {
		String expBytes = "C1" +
				"0044" +
				"00000001" +
				"0190" +
				"07" +
				"000B" +
				"0003" +
				"001005BD" +
				"617364657200000000000000000000000000000000000000000000000000" +
				"2000" +
				"0900" +
				"61007300640065007200" +
				"0900" +
				"2000" +
				"0000";
		ClilocMessage msg = new ClilocMessage(TestingFactory.createTestMobile(1, "asder"));
		assertEquals(expBytes, MessagesUtils.getHexString(msg.encode().array()));
	}
	
	@Test
	public void itemClilocMessage() {
		String expBytes = "C1" +
				"0032" +
				"00004242" +
				"0042" +
				"06" +
				"000B" +
				"0003" +
				"000F90A2" + // (1020000 decimal = hex -->) 0xF9060 + 0x42
				"000000000000000000000000000000000000000000000000000000000000" +
				"0000";
		ClilocMessage msg = new ClilocMessage(new UOItem(0x4242, 0x42, 0x43));
		assertEquals(expBytes, MessagesUtils.getHexString(msg.encode().array()));
	}

	@Test
	public void updatePlayerRetainsRunningInfo() throws IOException {
		Mobile mobile = TestingFactory.createTestMobile(1, "myname");
		assertEquals( mobile.getDirectionWithRunningInfo(), 
				new UpdatePlayer(mobile).getDirectionWithRunningInfo());
		mobile.setRunning(true);
		assertEquals( mobile.getDirectionWithRunningInfo(), 
				new UpdatePlayer(mobile).getDirectionWithRunningInfo());
	}
}
