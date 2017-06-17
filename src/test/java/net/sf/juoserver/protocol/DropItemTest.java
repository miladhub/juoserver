package net.sf.juoserver.protocol;

import static org.junit.Assert.*;

import net.sf.juoserver.protocol.DropItem;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

public class DropItemTest {

	@Test
	public void dropOnTheGround() throws DecoderException {
		byte[] bytes = Hex.decodeHex( "08000004B302E8087800FFFFFFFF".toCharArray() );
		DropItem di = new DropItem(bytes);
		assertEquals(0x4B3, di.getItemSerial());
		assertEquals(0x02E8, di.getTargetX());
		assertEquals(0x0878, di.getTargetY());
		assertEquals(0, di.getTargetZ());
		assertEquals(0xFFFFFFFF, di.getTargetContainerSerial());
		assertTrue(di.isDroppedOnTheGround());
	}
	
	@Test
	public void dropOnContainer() throws DecoderException {
		byte[] bytes = Hex.decodeHex( "08000004B302E808780000001234".toCharArray() );
		DropItem di = new DropItem(bytes);
		assertEquals(0x4B3, di.getItemSerial());
		assertEquals(0x02E8, di.getTargetX());
		assertEquals(0x0878, di.getTargetY());
		assertEquals(0, di.getTargetZ());
		assertEquals(0x1234, di.getTargetContainerSerial());
		assertFalse(di.isDroppedOnTheGround());
	}
	
}
