package net.sf.juoserver.model;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.LoginManager;
import net.sf.juoserver.api.NoSuchCharacterException;
import net.sf.juoserver.api.WrongPasswordException;

public class UOLoginManager implements LoginManager {
	private final Map<Integer, Account> authorizedAccountsByKeys = new ConcurrentHashMap<Integer, Account>();
	private final Core core;
	
	public UOLoginManager(Core core) {
		super();
		this.core = core;
	}

	@Override
	public Account login(String username, String password) {
		Account account = core.findAccountByUsername(username);
		
		if (account == null) {
			throw new NoSuchCharacterException(username);
		}
		
		if (!core.authenticate(account, password)) {
			throw new WrongPasswordException(username);
		}
		
		return account;
	}
	
	@Override
	public int generateAuthenticationKey(Account account) {
		int key = new Random().nextInt();
		authorizedAccountsByKeys.put(key, account);
		return key;
	}
	
	@Override
	public Account getAuthorizedAccount(int key) {
		return authorizedAccountsByKeys.get(key);
	}
}
