package net.sf.juoserver.api;

/**
 * This exception signals that an error occurred while notifying messages
 * between the server's various components.
 */
public class IntercomException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public IntercomException() {
		super();
	}

	public IntercomException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntercomException(String message) {
		super(message);
	}

	public IntercomException(Throwable cause) {
		super(cause);
	}
}
