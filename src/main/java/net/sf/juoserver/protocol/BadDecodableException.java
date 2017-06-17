package net.sf.juoserver.protocol;

import net.sf.juoserver.api.Decodable;

/**
 * Exception indicating that a {@link Decodable} message
 * was not properly coded (e.g., it misses a public constructor
 * taking a byte[] as its only argument).
 */
public class BadDecodableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BadDecodableException() {
		super();
	}

	public BadDecodableException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadDecodableException(String message) {
		super(message);
	}

	public BadDecodableException(Throwable cause) {
		super(cause);
	}
}
