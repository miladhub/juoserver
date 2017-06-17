package net.sf.juoserver.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.Point2D;
import net.sf.juoserver.model.PointInSpace;
import net.sf.juoserver.model.Position;
import net.sf.juoserver.model.UOContainer;
import net.sf.juoserver.model.UOItem;
import net.sf.juoserver.protocol.ClilocMessage;
import net.sf.juoserver.protocol.ContainerItems;
import net.sf.juoserver.protocol.DoubleClick;
import net.sf.juoserver.protocol.DrawContainer;
import net.sf.juoserver.protocol.DropItem;
import net.sf.juoserver.protocol.LookRequest;
import net.sf.juoserver.protocol.PickUpItem;
import net.sf.juoserver.protocol.WearItem;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

public class ItemsTest extends AbstractGameControllerTest {
	private final Item item = new UOItem(45, 46, 3);
	private final Container backpack = new UOContainer(50, 51, 52, 53, Arrays.asList(item),
			new HashMap<Item, Point2D>());
	
	@Before
	public final void configureItems() throws IOException {
		areAllRetrievableByCore(item, backpack);
	}
	
	private void areAllRetrievableByCore(Item... items) {
		for (final Item item : items) {
			context.checking(new Expectations() {{
				allowing(core).findItemByID(item.getSerialId());
					will(returnValue(item));
			}});
		}
	}
	
	@Test
	public void dropItemOnTheGround() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).dropItem(42, true, 0xFFFFFFFF, new PointInSpace(10, 20, 30));
		}});
		gameController.handle(new DropItem(42, 10, 20, 30, 0xFFFFFFFF));
	}
	
	@Test
	public void dropItemOnAnotherContainer() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).dropItem(42, false, 100, new PointInSpace(10, 20, 30));
		}});
		gameController.handle(new DropItem(42, 10, 20, 30, 100));
	}
	
	@Test
	public void pickUpItemIsIgnored() throws IOException {
		gameController.handle(new PickUpItem(item.getSerialId(), (short) 1));
	}
	
	@Test
	public void wearItem() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).wearItemOnMobile(Layer.Pants, item.getSerialId());
		}});
		gameController.handle(new WearItem(item, mobile, Layer.Pants));
	}
	
	@Test
	public void openBackpack() throws IOException {
		assertEquals(Arrays.asList(new DrawContainer(backpack), new ContainerItems(backpack)),
				gameController.handle(new DoubleClick(backpack.getSerialId(), false)));
	}
	
	@Test
	public void subsequentMessagesHoldRespectiveItemsLists() {
		@SuppressWarnings("serial")
		UOContainer container = new UOContainer(100, 101, 102, 103, Arrays.asList(new UOItem(1, 2, 3)), new HashMap<Item, Point2D>() {{
			put(new UOItem(1, 2, 3), new Position(0, 1));
		}});
		ContainerItems v1 = new ContainerItems(container);
		container.addItem(new UOItem(4, 5, 6), new Position(7, 8));
		ContainerItems v2 = new ContainerItems(container);
		assertTrue(v1.encode().array().length < v2.encode().array().length);
	}
	
	@Test
	public void singleClickOnItem() throws IOException {
		context.checking(new Expectations() {{
			oneOf(core).findMobileByID(item.getSerialId());
				will(returnValue(null));
		}});
		assertEquals(new ClilocMessage(item), gameController.handle(new LookRequest(item.getSerialId())));
	}
}
