package net.sf.juoserver.api;

public class WrongPasswordException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public WrongPasswordException(String username) {
		super("Wrong password for user " + username);
	}
}
