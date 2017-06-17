package net.sf.juoserver.protocol;

import static org.junit.Assert.*;
import net.sf.juoserver.api.MessageType;
import net.sf.juoserver.protocol.UnicodeSpeechRequest;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class UnicodeSpeechRequestTest {

	static final String MSG_HEX_BYTES =
		"AD00380000340003495441000054007200790069006E00670" +
		"0200074006F00200073007000650061006B00200068006500" +
		"72006500210000";

	@Test
	public void parseMessage() throws DecoderException {
		byte[] bytes = Hex.decodeHex( MSG_HEX_BYTES.toCharArray() );
		UnicodeSpeechRequest msg = new UnicodeSpeechRequest(bytes);
		assertEquals(MessageType.Regular, msg.getMessageType());
		assertEquals(0x34, msg.getHue());
		assertEquals(3, msg.getFont());
		assertEquals("ITA", msg.getLanguage());
		assertEquals("Trying to speak here!", msg.getText());
	}
	
}
