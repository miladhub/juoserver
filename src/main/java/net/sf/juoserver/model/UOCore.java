package net.sf.juoserver.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.DataManager;
import net.sf.juoserver.api.FileReadersFactory;
import net.sf.juoserver.api.Configuration;
import net.sf.juoserver.api.LoadException;
import net.sf.juoserver.api.MapFileReader;
import net.sf.juoserver.api.MapLocation;
import net.sf.juoserver.api.MapTile;
import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.Point2D;

/**
 * The <b>Core</b> facade.
 * <p/>
 * Holds information about everything: tiles, mobiles, etc.
 */
public final class UOCore implements Core {
	private static final int MOBILES_MAX_SERIAL_ID = 0x3FFFFFFF;
	//TODO: make this private
	public static final int ITEMS_MAX_SERIAL_ID = MOBILES_MAX_SERIAL_ID + 1;
	private static final int OBJECTS_MAX_SERIAL_ID = 0x7FFFFFFF;
	
	/**
	 * Currently managed mobiles.
	 */
	private final Map<Integer, Mobile> mobilesBySerialId = new HashMap<Integer, Mobile>();
	
	/**
	 * Currently managed items.
	 */
	private final Map<Integer, Item> itemsBySerialId = new HashMap<Integer, Item>();
	private final Map<Item, Container> containersByContainedItems = new HashMap<Item, Container>();
	
	/**
	 * The accounts, by username.
	 */
	private final Map<String, Account> accounts = new HashMap<String, Account>();
	private final Configuration configuration;
	private final FileReadersFactory fileReadersFactory;
	private final DataManager dataManager;
	
	/**
	 * Map reader.
	 */
	private MapFileReader mapReader;
	
	public UOCore(FileReadersFactory fileReadersFactory, DataManager dataManager, Configuration configuration) {
		super();
		this.configuration = configuration;
		this.fileReadersFactory = fileReadersFactory;
		this.dataManager = dataManager;
	}

	@Override
	public void init() {
		try {
			// TODO: coordinate the map index (the '0') with the number that we provide when we send
			// the GeneralInformationSetCursorHueSetMap message.
			mapReader = fileReadersFactory.createMapFileReader(new File(configuration.getUOPath()
					+ File.separator + "map0.mul"), 4096);
		} catch (FileNotFoundException e) {
			throw new LoadException(e);
		}
		
		loadData();
	}

	private void addItems(Point2D mob, Collection<? extends Item> items) {
		for (Item it : items) {
			itemsBySerialId.put(it.getSerialId(), it);
			if (it instanceof Container) {
				Container container = (Container) it;
				addItems(mob, container.getItems());
				for (Item item : container.getItems()) {
					containersByContainedItems.put(item, container);
				}
			}
		}
	}

	private void loadData() {
		for (Mobile mobile : dataManager.loadMobiles()) {
			mobilesBySerialId.put(mobile.getSerialId(), mobile);
		}
		
		for (Account account : dataManager.loadAccounts()) {
			accounts.put(account.getUsername(), account);
		}
		
		for (Mobile mob : mobilesBySerialId.values()) {
			addItems(mob, mob.getItems().values());
		}
	}
	
	/**
	 * Retrieves the map tile definition of the given coordinates.
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @return the map tile definition of the given coordinates
	 */
	@Override
	public MapTile getTile(final int x, final int y) {
		return mapReader.getEntryAt(new MapLocation() {
			@Override public int getX() { return x; }
			@Override public int getY() { return y; }
		});
	}
	
	@Override
	public Mobile findMobileByID(int serialID) {
		if (!isMobile(serialID)) {
			return null;
		}
		return mobilesBySerialId.get(serialID);
	}
	
	@Override
	public Item findItemByID(int serialID) {
		if (!isItem(serialID)) {
			return null;
		}
		return itemsBySerialId.get(serialID);
	}
	
	private boolean isMobile(int serialID) {
		return serialID > 0 && serialID <= UOCore.MOBILES_MAX_SERIAL_ID;
	}

	private boolean isItem(int serialID) {
		return serialID >= UOCore.ITEMS_MAX_SERIAL_ID && serialID <= OBJECTS_MAX_SERIAL_ID;
	}

	/**
	 * Retrieves an {@link Account} by username and password; the password is
	 * needed to check the credentials.
	 * 
	 * @param username username
	 * @return the {@link Account} matching the provided username, or <tt>null</tt>
	 * if no such account could be found
	 */
	@Override
	public Account findAccountByUsername(String username) {
		return accounts.get(username);
	}
	
	/**
	 * Attempts to authenticate the given account with the specified password.
	 * 
	 * @param account account
	 * @param password password
	 * @return <tt>true</tt> if and only if the account could be authenticated
	 * with the provided password
	 */
	@Override
	public boolean authenticate(Account account, String password) {
		return password != null && password.equals( account.getPassword() ); 
	}

	@Override
	public Container findContainerByContainedItem(Item item) {
		return containersByContainedItems.get(item);
	}

	@Override
	public void removeItemFromContainer(Item item) {
		containersByContainedItems.remove(item);
	}

	@Override
	public void addItemToContainer(Item item, Container container) {
		containersByContainedItems.put(item, container);
	}
}
