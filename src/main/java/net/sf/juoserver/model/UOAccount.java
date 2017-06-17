package net.sf.juoserver.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.juoserver.api.Account;

public class UOAccount implements Account {
	public static Account createAccount(int accountId, String username,
			String password, int... mobilesSerialIds) {
		UOAccount account = new UOAccount(accountId, username, password);
		for (int mobileSerialId : mobilesSerialIds) {
			account.addMobileSerialId(mobileSerialId);
		}
		return account;
	}

	private int accountId;
	private String username;
	private String password;

	/**
	 * Account's characters serial IDs, indexed by their position in the login
	 * screen.
	 */
	private List<Integer> charSerialsByPositions = new ArrayList<Integer>();

	public UOAccount(int accountId, String username, String password) {
		super();
		this.accountId = accountId;
		this.username = username;
		this.password = password;
	}

	@Override
	public void addMobileSerialId(int serialId) {
		charSerialsByPositions.add(serialId);
	}

	@Override
	public int getAccountId() {
		return accountId;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Retrieves an account's characters serial IDs, given its position in the
	 * login screen.
	 * 
	 * @param position
	 *            position of the account's character within the login screen
	 * @return the account's characters serial IDs having the specified position
	 *         in the login screen.
	 */
	@Override
	public int getCharacterSerialIdByPosition(int position) {
		return charSerialsByPositions.get(position);
	}
	
	@Override
	public List<Integer> getCharactersSerials() {
		return charSerialsByPositions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UOAccount other = (UOAccount) obj;
		if (accountId != other.accountId)
			return false;
		return true;
	}
}
