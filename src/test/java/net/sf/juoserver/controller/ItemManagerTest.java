package net.sf.juoserver.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;

import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Point2D;
import net.sf.juoserver.model.UOContainer;
import net.sf.juoserver.model.UOItem;
import net.sf.juoserver.protocol.ContainerItems;
import net.sf.juoserver.protocol.DrawContainer;
import net.sf.juoserver.protocol.ItemManager;

import org.junit.Test;

public class ItemManagerTest {
	@Test
	public void openContainer() {
		Item item = new UOItem(1, 2, 3);
		Container cont = new UOContainer(42, 43, 44, 45, Arrays.asList(item), new HashMap<Item, Point2D>());
		ItemManager im = new ItemManager();
		assertEquals(Arrays.asList(new DrawContainer(cont), new ContainerItems(cont)), im.use(cont));
	}
}
