package net.sf.juoserver.api;

import java.nio.ByteBuffer;

/**
 * Public contract of a message.
 */
public interface Message {
	/**
	 * @return this message's code
	 */
	int getCode();
	
	/**
	 * @return this message's length in bytes
	 */
	int getLength();

	/**
	 * Encodes this message into a byte array packet.
	 * <p/>
	 * The array is wrapped in a byte buffer in order to let subclasses append
	 * their contents.
	 * 
	 * @return the byte array representation (a.k.a. <i>packet</i>) of this message
	 */
	ByteBuffer encode();

	/**
	 * @return {@code true} if and only if this message should be compressed when sent by the server
	 */
	boolean isCompressed();
}
