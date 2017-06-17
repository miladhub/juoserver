package net.sf.juoserver.protocol;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.MessageType;
import net.sf.juoserver.protocol.MessagesUtils;
import net.sf.juoserver.protocol.UnicodeSpeech;
import net.sf.juoserver.protocol.UnicodeSpeechRequest;

public class UnicodeSpeechTest {

	private static final String NAME = "MyMob";
	private static final String LANGUAGE = "ENG";
	private static final int HUE = 3;
	private static final int FONT = 4;
	private static final String TXT = "Hi there!";
	private UnicodeSpeech speech;

	@Before
	public void createMessage() throws DecoderException {
		UnicodeSpeechRequest req = new UnicodeSpeechRequest(
				MessageType.Regular, HUE, FONT, LANGUAGE, TXT);
		speech = new UnicodeSpeech(TestingFactory.createTestMobile(1, NAME), req);
	}

	@Test
	public void encode() {
		byte[] bytes = speech.encode().array();
		String hex = new String(Hex.encodeHex(bytes)).toUpperCase();
		assertEquals(getExpectedHex(), hex);
	}

	private String getExpectedHex() {
		return "AE" + // Code
				"0044" + // Length (in hex) = 48 + (9 + 1) * 2
				"00000001" + // Mobile serial
				"0190" + // Body type
				"00" + // Message type (regular)
				"0003" + // Hue
				"0004" + // Font
				padStringAndEncodeToHex(LANGUAGE, 4) + // Language
				padStringAndEncodeToHex(NAME, 30) + // Name
				unicodeNullTerminatedStringToHex(TXT); // TXT
	}
	
	private static String unicodeNullTerminatedStringToHex(String txt) {
		ByteBuffer txtBuff = ByteBuffer.allocate(MessagesUtils.getUnicodeNullTerminatedStringLength(txt));
		MessagesUtils.putUnicodeString(txtBuff, txt);
		return MessagesUtils.getHexString(txtBuff.array());
	}
	
	private static String padStringAndEncodeToHex(String str, int length) {
		return new String( Hex.encodeHex(MessagesUtils.padString(str, length), false) );
	}
}
