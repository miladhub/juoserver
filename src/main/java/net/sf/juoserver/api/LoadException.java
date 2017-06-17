package net.sf.juoserver.api;

/**
 * Exception signaling that something went wrong while loading objects (either
 * the map, or the mobiles, etc).
 */
public class LoadException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LoadException() {
		super();
	}

	public LoadException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoadException(String message) {
		super(message);
	}

	public LoadException(Throwable cause) {
		super(cause);
	}
}
