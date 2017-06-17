package net.sf.juoserver.api;

public interface LoginManager {
	Account login(String username, String password) throws NoSuchCharacterException, WrongPasswordException;
	int generateAuthenticationKey(Account account);
	Account getAuthorizedAccount(int key);
}
