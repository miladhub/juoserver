package net.sf.juoserver.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Packets utility class.
 */
public class Packets {
	private static final String UNICODE = "unicode";
	private static final String HEXSTRING = "hexstring";
	private static final String DECOMPRESS = "decompress";
	private static final String SYNTAX = "Syntax:\n\t" + Packets.class.getName() + 
		" ( (d | decompress) | (h | hexstring) | (u | unicode) ) [<ARG>],\n" +
		"\nCommands:\n" +
		"\t" + DECOMPRESS + ": \thuffman-decodes the bytes whose hexadecimal value is represented by ARG\n" +
		"\t\t\tin the format 0xXX, 0xYY, ... where X, Y are hexadecimal numbers (0..F)\n" +
		"\t" + HEXSTRING + ": \ttranslates into a string the bytes whose hexadecimal value is represented by ARG\n" +
		"\t\t\tin the format XXYYZZ... where X, Y, Z are hexadecimal numbers (0..F)\n" +
		"\t" + UNICODE + ": \tlike hexstrings, but assumes that the characters are encoded with 2 bytes instead\n" +
		"\t\t\tof one, so ARG must be in the format 00XX00YY00ZZ\n" +
		"\t" + "ARG is never mandatory: if absent, the string will be read from the command line (thus pipes can be used).\n";

	public static void main(String[] args) throws DecoderException, IOException {
		if (args.length == 0) {
			System.err.println(SYNTAX);
			System.exit(1);
		}
		
		String cmd = args[0];
		String arg = null;
		
		if (args.length == 2) {
			arg = args[1];
		} else if (args.length == 1) {
			System.err.println("Reading lines (ctrl-D to interrupt the stream)...");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			StringBuilder sb = new StringBuilder();
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				sb.append(line + ' ');
				i++;
			}
			System.err.println("Read " + i + " lines.");
			arg = sb.toString();
		} else {
			System.err.println(SYNTAX);
			System.exit(1);
		}
		
		arg = cleanString(arg);
		
		if (cmdMatches(cmd, DECOMPRESS)) {
			System.out.println(decompressHexString( arg ));
		} else if (cmdMatches(cmd, HEXSTRING)) {
			System.out.println(decodeHexString( arg ));
		} else if (cmdMatches(cmd, UNICODE)) {
			System.out.println(decodeUnicodeHexString( arg ));
		}
	}

	private static boolean cmdMatches(String cmd, String name) {
		return name.equals(cmd) || name.substring(0, 1).equals(cmd);
	}

	/**
	 * Translates the given bytes (i.e. the string representation of their hex format)
	 * in a string representation, i.e. interpreting them as characters.
	 * <p/>
	 * For example, the expression
	 * <pre>decodeHexString("776172")</pre>
	 * will return the string <tt>war</tt>.
	 * @param hexString string representation of the (hex-) bytes to translate into a string 
	 * @return string translation whose characters are the converted from each byte of
	 * the given byte sequence
	 * @throws DecoderException in case of hexadecimal numbers decodification errors
	 */
	private static String decodeHexString(String hexString) throws DecoderException {
		return new String( Hex.decodeHex(hexString.toCharArray()) );
	}
	
	/**
	 * Translates the given bytes (i.e. the string representation of their hex format)
	 * in a string representation, i.e. interpreting them as characters, assuming that
	 * the given bytes are in unicode format, meaning that each character is represented
	 * by two bytes, not one. The first, most significant byte will be stripped of.
	 * <p/>
	 * For example, the expression
	 * <pre>decodeUnicodeHexString("007700610072")</pre>
	 * will return the string <tt>war</tt>.
	 * @param unicodeHexString string representation of the (hex-) unicode bytes to translate into a string 
	 * @return string translation whose characters are the converted from each byte of
	 * the given unicode byte sequence
	 * @throws DecoderException in case of hexadecimal numbers decodification errors
	 */
	private static String decodeUnicodeHexString(String unicodeHexString) throws DecoderException {
		if (unicodeHexString == null || unicodeHexString.length() % 4 != 0) {
			// Each byte is made of two characters, so a two-byte sequence
			// is made up by four characters.
			throw new IllegalArgumentException("String must be made a 2-bytes sequence");
		}
		
		int nBytePairs = unicodeHexString.length() / 4;
		// I'm gonna strip of the 00
		StringBuilder sb = new StringBuilder(unicodeHexString.length() / 2);
		for (int i = 0; i < nBytePairs; i++) {
			sb.append(unicodeHexString.substring(i * 4 + 2, (i + 1) * 4));
		}
		
		return decodeHexString(sb.toString());
	}
	
	/**
	 * Decompresses (i.e. decodes with the Huffman algorithm) the source hex string
	 * and returns the concatenation of each decoded byte's string representations
	 * in hexadecimal format.
	 * <p/>
	 * For example, the expression:
	 * <pre>decodedHexString("0x76, 0x34")</pre>
	 * will return <tt>7300</tt>, which is the decompressed version of the packet
	 * <tt>"0x76, 0x34"</tt>, which in turn is a compressed <i>ping</i> message
	 * (sent by the server - the client never performs compression).
	 * 
	 * @param source
	 *            string to be decoded and printed out
	 * @return the decoded hex string, printed in hexadecimal format
	 * @throws DecoderException in case of hexadecimal numbers decodification errors
	 */
	private static String decompressHexString(String source)
			throws DecoderException {
		return Hex.encodeHexString(
				Huffman.decode(Hex.decodeHex(source.toCharArray())))
				.toUpperCase();
	}
	
	private static String cleanString(String source) {
		return source.replaceAll("0x", "").replaceAll(",", "").replaceAll("\\s*", "");
	}
}