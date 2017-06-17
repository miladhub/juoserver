package net.sf.juoserver.protocol;

import static org.junit.Assert.assertEquals;

import net.sf.juoserver.protocol.PickUpItem;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class PickUpItemTest {
	
	@Test
	public void receivePickUpItem() throws DecoderException {
		byte[] bytes = Hex.decodeHex( "074000001A0001".toCharArray() );
		PickUpItem pickUpItem = new PickUpItem(bytes);
		assertEquals(0x4000001A, pickUpItem.getItemSerialId());
		assertEquals(1, pickUpItem.getAmount());
	}
}
