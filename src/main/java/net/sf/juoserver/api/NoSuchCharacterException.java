package net.sf.juoserver.api;

public class NoSuchCharacterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoSuchCharacterException(String username) {
		super("No such character: " + username);
	}
}
