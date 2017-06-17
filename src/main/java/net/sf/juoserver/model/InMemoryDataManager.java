package net.sf.juoserver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.sf.juoserver.api.DataManager;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.Point2D;
import net.sf.juoserver.api.RaceFlag;
import net.sf.juoserver.api.SexRace;
import net.sf.juoserver.api.StatusFlag;
import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Mobile;

public class InMemoryDataManager implements DataManager {
	private static final int ACCOUNT_ID = 0;

	private static final int FIRST_SERIAL_ID = 1;
	private static final int SECOND_SERIAL_ID = 2;

	@Override
	public List<Mobile> loadMobiles() {
		return Arrays.asList(
				dressUp(new UOMobile(FIRST_SERIAL_ID, "Loller", 10,
				10, false, StatusFlag.UOML, SexRace.MaleHuman, 10, 10, 10, 10,
				10, 10, 10, 1000, 5, 5, 50, RaceFlag.Human)),
				dressUp(new UOMobile(SECOND_SERIAL_ID,
				"Asder", 10, 10, false, StatusFlag.UOML, SexRace.MaleHuman, 10,
				10, 10, 10, 10, 10, 10, 1000, 5, 5, 50, RaceFlag.Human)));
	}

	private Mobile dressUp(Mobile mobile) {
		// The items' serialIDs must NOT overlap, otherwise the last character "wearing" them will "steal" them from the others
		UOItem brick = new UOItem(mobile.getSerialId() + 1800 + UOCore.ITEMS_MAX_SERIAL_ID, 0x1F9E, 0);
		HashMap<Item, Point2D> positions = new HashMap<Item, Point2D>();
		positions.put(brick, new Point2D() {
			@Override
			public int getY() {
				return 0x7E;
			}
			@Override
			public int getX() {
				return 0x68;
			}
		});
		mobile.setItemOnLayer(Layer.Backpack, new UOContainer(mobile.getSerialId() + 1000 + UOCore.ITEMS_MAX_SERIAL_ID, 0x0E75, 0, 0x003C,
				Arrays.asList(brick),
				positions));
		mobile.setItemOnLayer(Layer.InnerTorso, new UOItem(mobile.getSerialId() + 0x1A + UOCore.ITEMS_MAX_SERIAL_ID, 0x1F7B, 0x7E));
		mobile.setItemOnLayer(Layer.OuterTorso, new UOItem(mobile.getSerialId() + 1200 + UOCore.ITEMS_MAX_SERIAL_ID, 0x1F03, 0x04D9));
		mobile.setItemOnLayer(Layer.FirstValid, new UOItem(mobile.getSerialId() + 1300 + UOCore.ITEMS_MAX_SERIAL_ID, 0x13FF, 0));
		mobile.setItemOnLayer(Layer.MiddleTorso, new UOItem(mobile.getSerialId() + 1400 + UOCore.ITEMS_MAX_SERIAL_ID, 0x13BF, 0x0381));
		mobile.setItemOnLayer(Layer.Pants, new UOItem(mobile.getSerialId() + 1500 + UOCore.ITEMS_MAX_SERIAL_ID, 0x13BE, 0x0090));
		mobile.setItemOnLayer(Layer.Shoes, new UOItem(mobile.getSerialId() + 1600 + UOCore.ITEMS_MAX_SERIAL_ID, 0x26AF, 0x06A8));
		mobile.setItemOnLayer(Layer.Hair, new UOItem(mobile.getSerialId() + 1700 + UOCore.ITEMS_MAX_SERIAL_ID, 0xA03B, 0x044E));
		return mobile;
	}

	@Override
	public List<Account> loadAccounts() {
		return new ArrayList<Account>(Arrays.asList(UOAccount
				.createAccount(ACCOUNT_ID, "admin", "admin", FIRST_SERIAL_ID,
						SECOND_SERIAL_ID)));
	}
}
