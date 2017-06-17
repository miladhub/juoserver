package net.sf.juoserver.api;

import java.util.List;

public interface Account {
	void addMobileSerialId(int serialId);

	int getAccountId();

	String getUsername();

	String getPassword();

	/**
	 * Retrieves an account's characters serial IDs, given its position in the
	 * login screen.
	 * 
	 * @param position
	 *            position of the account's character within the login screen
	 * @return the account's characters serial IDs having the specified position
	 *         in the login screen.
	 */
	int getCharacterSerialIdByPosition(int position);

	List<Integer> getCharactersSerials();
}
