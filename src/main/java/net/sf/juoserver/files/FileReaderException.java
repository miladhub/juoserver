package net.sf.juoserver.files;

/**
 * Exception occurred while accessing game files.
 */
public class FileReaderException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileReaderException() {
		super();
	}

	public FileReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileReaderException(String message) {
		super(message);
	}

	public FileReaderException(Throwable cause) {
		super(cause);
	}
}
