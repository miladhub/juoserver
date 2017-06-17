package net.sf.juoserver.api;

/**
 * Contract for a class capable of providing {@link MessageDecoder}s
 * by examining the first byte of a given raw message.
 */
public interface MessageDecoderProvider {
	/**
	 * Retrieves a {@link MessageDecoder} for the message
	 * identified by the provided contents's {{@code firstByte}.
	 * @param firstByte raw contents' first byte 
	 * @return a {@link MessageDecoder} for the message
	 * identified by the provided contents's first byte
	 */
	MessageDecoder getDecoder(byte firstByte);
}