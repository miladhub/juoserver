package net.sf.juoserver.api;

import java.util.List;

public interface DataManager {
	List<Mobile> loadMobiles();

	List<Account> loadAccounts();
}
