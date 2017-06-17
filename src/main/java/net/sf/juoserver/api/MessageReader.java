package net.sf.juoserver.api;

import java.util.List;

/**
 * Contract for a class capable of
 * reading messages from byte arrays.
 */
public interface MessageReader {
	/**
	 * Returns a list of {@link Message}s by parsing the provided byte array packet chunk.
	 * @param packetContents contents of the actual messages
	 * @return a list of {@link Message}s by parsing the provided byte array packet chunk
	 * @see <a href="http://docs.polserver.com/packets/">UO Protocol (by POL)</a>
	 */
	List<Message> readMessages(byte[] packetContents);
}
