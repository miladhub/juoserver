package net.sf.juoserver.protocol;

import java.io.Serializable;
import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Message;

/**
 * Base class for messages.
 * <p/>
 * Messages can be sent by the client or the server, or both (e.g. {@link ClientVersion}).
 * <p/>
 * Messages sent by clients (e.g. {@link CharacterSelect}) should:
 * <ul>
 * <li>Be annotated with {@link Decodable},</li>
 * <li>Provide a <b>public</b> constructor taking a single
 * <tt>byte</tt> array.</li>
 * </ul>
 * Messages sent by the server (e.g. {@link CharacterList}) should:
 * <ul>
 * <li>Override the {@link #encode()} method,</li>
 * <li>Possibly override the {@link #isCompressed()} method.</li>
 * </ul>
 * By creating a client message by following the above steps,
 * the server is already able to receive that message.
 * <p/>
 * It is however also necessary to provide the ability to
 * <b>process</b> that client message, by adding a handler
 * to the {@link GameController} class and thus letting it
 * reply to the client message with a server message.
 */
public abstract class AbstractMessage implements Message, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String UTF8 = "UTF8";
	protected static final int NO_CODE = -1;
	
	/**
	 * This message's code (will be the first byte).
	 */
	private int code;
	
	/**
	 * This message's length in bytes.
	 */
	private int length;
	
	/**
	 * Builds a message from the code that identifies it.
	 * This code is the first byte in the message packet.
	 * @param code message's identification code
	 */
	public AbstractMessage(int code, int length) {
		this.code = code;
		this.length = length;
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = ByteBuffer.allocate(getLength());
		bb.put((byte) (getCode() & 0xFF));
		return bb;
	}
	
	/**
	 * {@inheritDoc}
	 * <p/>
	 * Default value is <tt>true</tt>.
	 */
	@Override
	public boolean isCompressed() {
		return true;
	}
	
	@Override
	public final int getCode() {
		return code;
	}
	
	@Override
	public final int getLength() {
		return length;
	}
	
	@Override
	public abstract int hashCode();
	
	@Override
	public abstract boolean equals(Object obj);
	
	/**
	 * @return this message's name
	 */
	public final String getName() {
		return getClass().getSimpleName() + getHexCodeSymbol();
	}

	/**
	 * Returns a new {@link ByteBuffer} wrapping the packet contents, excluding
	 * the first byte (the packet code).
	 * <p/>
	 * Note that the returned buffer will be backed up by the provided array,
	 * therefore modifications to the buffer will change the array and vice
	 * versa.
	 * 
	 * @param contents packet contents
	 * @return a new {@link ByteBuffer} wrapping the packet contents,
	 * excluding the first byte (the packet code)
	 * @see #wrapContents(int, byte[])
	 */
	protected final ByteBuffer wrapContents(byte[] contents) {
		return wrapContents(1, contents);
	}
	
	/**
	 * Returns a new {@link ByteBuffer} wrapping the packet contents, starting
	 * from the specified index, inclusive.
	 * <p/>
	 * Note that the returned buffer will be backed up by the provided array,
	 * therefore modifications to the buffer will change the array and vice
	 * versa.
	 * 
	 * @param fromIndexInclusive starting index where to start reading the
	 * contents, inclusive
	 * @param contents packet contents
	 * @return a new {@link ByteBuffer} wrapping the packet contents,
	 * excluding the first byte (the packet code)
	 * @see ByteBuffer#wrap(byte[], int, int)
	 */
	protected final ByteBuffer wrapContents(int fromIndexInclusive, byte[] contents) {
		return ByteBuffer.wrap(contents, fromIndexInclusive, getLength() - fromIndexInclusive);
	}
	
	private String getHexCodeSymbol() {
		if (getCode() == NO_CODE) {
			return "";
		}
		return "[0x" + getHexCode() + "]";
	}

	private String getHexCode() {
		return MessagesUtils.getHexString(new byte[] { Integer.valueOf(getCode()).byteValue() });
	}
}
