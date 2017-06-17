package net.sf.juoserver.protocol;

public class MessageReaderException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MessageReaderException() {
		super();
	}

	public MessageReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageReaderException(String message) {
		super(message);
	}

	public MessageReaderException(Throwable cause) {
		super(cause);
	}
}
