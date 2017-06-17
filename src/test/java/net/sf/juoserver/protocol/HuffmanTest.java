package net.sf.juoserver.protocol;

import static org.junit.Assert.*;

import java.util.Arrays;

import net.sf.juoserver.protocol.Huffman;

import org.junit.Before;
import org.junit.Test;

public class HuffmanTest {
	
	private Huffman encoder;
	private short endByte, endNByte;
	
	@Before
	public void setUp() {
		encoder = new Huffman();
		endByte = Huffman.encodeTree[256][1];
		endNByte = Huffman.encodeTree[256][0];
	}
	
	@Test
	public void encodeTwo() {
		// encoding for number 2
		short enc = Huffman.encodeTree[ 2 ][1];

		// appending the encoding for number 256
		enc <<= endNByte;
		enc |= endByte;
		enc <<= 16 - (Huffman.encodeTree[ 2 ][0] + endNByte); // Padding
		
		byte[] encoded = encoder.encode(new byte[]{ 2 });
		
		assertTrue( Arrays.equals(getBytes(enc), encoded) );
	}
	
	@Test
	public void decodeTwo() {
		byte[] two = new byte[]{ 2 };
		byte[] encodedTwo = encoder.encode( two );
		assertTrue(Arrays.equals(two, Huffman.decode( encodedTwo )));
	}
	
	@Test
	public void encodeFour() {
		// encoding for number 4
		short enc = Huffman.encodeTree[ 4 ][1];

		// appending the encoding for number 256
		enc <<= endNByte;
		enc |= endByte;
		enc <<= 16 - (Huffman.encodeTree[ 4 ][0] + endNByte); // Padding
		
		byte[] encoded = encoder.encode(new byte[]{ 4 });
		
		assertTrue( Arrays.equals(getBytes(enc), encoded) );
	}
	
	@Test
	public void decodeFour() {
		byte[] four = new byte[]{ 4 };
		byte[] encodedFour = encoder.encode( four );
		assertTrue(Arrays.equals(four, Huffman.decode( encodedFour )));
	}
	
	@Test
	public void decodeArray() {
		byte[] original = new byte[]{ 1, 2, 3, 4, 7, 8, 14, 5, 6, 8, 9, 0, 35 };
		byte[] encoded = encoder.encode( original );
		byte[] decoded = Huffman.decode( encoded );
		assertTrue(Arrays.equals(original, decoded));
	}
	
	private byte[] getBytes(short enc) {
		byte[] bytes = new byte[2];
		bytes[1] = (byte) enc;
		bytes[0] = (byte) (enc >> 8);
		return bytes;
	}

	@Test
	public void appendByte() {
		assertTrue(Arrays.equals(new byte[]{1, 2, 3, 4},
				Huffman.appendByte((byte) 4, new byte[]{1, 2, 3}))); 
	}
}
