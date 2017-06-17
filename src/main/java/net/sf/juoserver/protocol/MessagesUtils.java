package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;

final class MessagesUtils {
	/**
	 * Reads a null-terminated (i.e., ending with a <tt>0</tt>) string
	 * from the specified buffer.
	 * 
	 * @param bb where to read the string from
	 * @return a null-terminated string from the specified buffer
	 */
	static String readNullTerminatedUnicodeString(ByteBuffer bb) {
		int oldPos = bb.position();
		CharBuffer cb = bb.asCharBuffer();
		StringBuilder sb = new StringBuilder(32);
		char ch;
		while ((ch = cb.get()) != 0) {
			sb.append(ch);
		}
		String txt = sb.toString();
		bb.position(oldPos + MessagesUtils.getUnicodeNullTerminatedStringLength(txt));
		return txt;
	}

	/**
	 * Returns the length of the provided string, when
	 * encoded in unicode and null-terminated.
	 * 
	 * @param txt the string
	 * @return the length of the provided string, when
	 * encoded in unicode and null-terminated
	 */
	static int getUnicodeNullTerminatedStringLength(String txt) {
		return (txt.length() + 1) * 2;
	}

	/**
	 * Pads a string into a byte array, provided its required length.
	 * <p/>
	 * Any extra bytes - i.e., those who exceed the string's length -
	 * are filled (padded) with zeroes.
	 * 
	 * @param str string
	 * @param length length of the newly created array
	 * @return a string into a byte array, provided its required length
	 */
	static byte[] padString(String str, int length) {
		return Arrays.copyOf(str.getBytes(), length);
	}

	/**
	 * Reads the length of the specified message from its second
	 * and third byte.
	 * 
	 * @param contents message contents
	 * @return the length of the specified message from its second
	 * and third byte
	 */
	static int getLengthFromSecondAndThirdByte(byte[] contents) {
		ByteBuffer bb = ByteBuffer.wrap( ArrayUtils.subarray(contents, 1, 3) );
		return bb.getShort();
	}

	/**
	 * Reads a string from the specified buffer, reading exactly the
	 * specified number of characters.
	 * 
	 * @param bb buffer to read the string from
	 * @param numChars number of characters to be read
	 * @return a string from the specified buffer, reading exactly the
	 * specified number of characters
	 */
	static final String readString(ByteBuffer bb, int numChars) {
		byte[] tmp = new byte[numChars];
		bb.get(tmp);
		return MessagesUtils.getString(tmp, 0, tmp.length - 1);
	}

	/**
	 * Returns the HEX representation of the specified message's code, 
	 * given its contents.
	 * 
	 * @param contents the message's contents
	 * @return the HEX representation of the specified message's code
	 */
	public static String getCodeHexString(byte[] contents) {
		return "0x" + MessagesUtils.getHexString( contents ).substring(0, 2);
	}

	/**
	 * Reads a string from the provided byte array. Any leading
	 * zeroes are trimmed. Bytes are decoded into characters
	 * using the <tt>UFT8</tt> character set.
	 * 
	 * @param contents byte array to read the string from
	 * @param start array index where to start reading the string from
	 * @param end maximum array index to read the string up to
	 * @return a string from the provided byte array
	 */
	public static String getString(byte[] contents, int start, int end) {
		return new String(
				Arrays.copyOfRange(contents, start, getMaxIndex(contents, start, end)),
				Charset.forName(AbstractMessage.UTF8) );
	}
	
	private static int getMaxIndex(byte[] contents, int from, int to) {
		if (from > to) {
			throw new IllegalArgumentException("from was higher than to");
		}
		int c = to;
		for (; c >= from; c--) {
			if (contents[c] != 0) break;
		}
		return c + 1;
	}

	/**
	 * Writes the provided string into the specified byte buffer,
	 * writing the zeroes after the characters. This is done by 
	 * using little-endian semantics.
	 * 
	 * @param bb byte array where to write the string into
	 * @param str the string to be written in the byte array
	 * @see ByteOrder#LITTLE_ENDIAN
	 */
	public static void putReverseUnicodeString(ByteBuffer bb, String str) {
		ByteOrder origOrd = bb.order();
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.asCharBuffer().put(str);
		bb.order(origOrd);
		bb.position(bb.position() + str.length() * 2);
	}

	/**
	 * Writes the provided string into the specified byte buffer.
	 * <p/>
	 * This method writes the zeroes before the characters, by 
	 * using big-endian semantics.
	 * 
	 * @param bb byte array where to write the string into
	 * @param str the string to be written in the byte array
	 * @see ByteOrder#BIG_ENDIAN
	 */
	public static void putUnicodeString(ByteBuffer bb, String str) {
		ByteOrder origOrd = bb.order();
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.asCharBuffer().put(str);
		bb.order(origOrd);
		bb.position(bb.position() + str.length() * 2);
	}

	/**
	 * Returns the HEX representation of the given byte array.
	 * 
	 * @param contents the byte array to be represented in HEX format
	 * @return the HEX representation of the given byte array
	 */
	public static String getHexString(byte[] contents) {
		return Hex.encodeHexString(contents).toUpperCase();
	}
}
