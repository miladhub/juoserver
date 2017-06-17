package net.sf.juoserver.api;

import net.sf.juoserver.protocol.MessageReaderException;

/**
 * Contract for a class capable of decoding a {@link Message}
 * from a raw bytes chunk.
 */
public interface MessageDecoder {
	/**
	 * Decodes a {@link Message} from a raw bytes chunk.
	 * @param contents
	 * @return the decoded message
	 * @throws MessageReaderException
	 */
	Message decode(byte[] contents);
}
