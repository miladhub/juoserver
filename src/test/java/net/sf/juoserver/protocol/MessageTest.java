package net.sf.juoserver.protocol;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;


import net.sf.juoserver.protocol.AbstractMessage;
import net.sf.juoserver.protocol.MessagesUtils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class MessageTest {

	private static final String UNICODE_MSG = "006D007300670000";
	
	@Test
	public void readMessage() throws DecoderException {
		ByteBuffer bb = ByteBuffer.wrap( Hex.decodeHex( UNICODE_MSG.toCharArray() ) );
		assertEquals("msg", MessagesUtils.readNullTerminatedUnicodeString(bb));
	}
	
	@Test
	public void readSequentialMessages() throws DecoderException {
		ByteBuffer bb = ByteBuffer.wrap( Hex.decodeHex( (UNICODE_MSG + UNICODE_MSG).toCharArray() ) );
		assertEquals("msg", MessagesUtils.readNullTerminatedUnicodeString(bb));
		assertEquals("msg", MessagesUtils.readNullTerminatedUnicodeString(bb));
	}
	
	@Test
	public void getName() {
		assertEquals("MyNamedMessage[0x42]", new MyNamedMessage(0x42, 10).getName());
	}
	
	@Test
	public void getNameNoCode() {
		assertEquals("MyNamedMessage", new MyNamedMessage(AbstractMessage.NO_CODE, 10).getName());
	}
	
	private static final class MyNamedMessage extends AbstractMessage {
		private static final long serialVersionUID = 1L;
		private MyNamedMessage(int code, int length) {
			super(code, length);
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
	
}
