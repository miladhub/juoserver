package net.sf.juoserver.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.Point2D;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

public class ItemComponentTest extends AbstractComponentTest {
	private final Item bottleWasInBackpack = new UOItem(10, 1, 2);
	private final Item rockWasOnTheGround = new UOItem(11, 1, 2);
	private final Item pants = new UOItem(12, 1, 2);
	private final Item shoesWereInBackpack = new UOItem(13, 1, 2);
	@SuppressWarnings("serial")
	private final Container backpack = new UOContainer(100, 1, 2, 3, Arrays.asList(bottleWasInBackpack, shoesWereInBackpack),
			new HashMap<Item, Point2D>() {{
				put(bottleWasInBackpack, new Position(10, 11));
				put(shoesWereInBackpack, new Position(12, 13));
			}});
	
	@Before
	public void createItems() throws IOException {
		context.checking(new Expectations() {{
			areAllRetrievableByCore(bottleWasInBackpack, rockWasOnTheGround, backpack, pants, shoesWereInBackpack);
			allowing(core).findContainerByContainedItem(bottleWasInBackpack);
				will(returnValue(backpack));
			allowing(core).findContainerByContainedItem(shoesWereInBackpack);
				will(returnValue(backpack));
			allowing(core).findContainerByContainedItem(rockWasOnTheGround);
				will(returnValue(null));
		}});
		
		assertTrue(backpack.getItems().contains(bottleWasInBackpack));
		assertFalse(backpack.getItems().contains(rockWasOnTheGround));
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
			oneOf(core).removeItemFromContainer(bottleWasInBackpack);
			
			oneOf(asderListener).containerChangedContents(backpack);
			oneOf(asderListener).itemChanged(bottleWasInBackpack, new PointInSpace(10, 20, 30));
			
			oneOf(lollerListener).itemDragged(bottleWasInBackpack, 1, asder, 0, new PointInSpace(10, 20, 30));
			oneOf(lollerListener).itemChanged(bottleWasInBackpack, new PointInSpace(10, 20, 30));
		}});
		
		asderSession.dropItem(bottleWasInBackpack.getSerialId(), true, 0xFFFFFFFF, new PointInSpace(10, 20, 30));
	}
	
	@Test
	public void dropItemInBackpack() throws IOException {
		context.checking(new Expectations() {{
			oneOf(core).addItemToContainer(rockWasOnTheGround, backpack);
			
			oneOf(asderListener).containerChangedContents(backpack);
		}});
		
		asderSession.dropItem(rockWasOnTheGround.getSerialId(), false, backpack.getSerialId(), new PointInSpace(10, 20, 30));
		
		assertEquals(new Position(10, 20), backpack.getPositionWithinContainer(rockWasOnTheGround));
	}
	
	@Test
	public void wearingItemNotifiesOtherMobiles() {
		asder.emptyLayer(Layer.Pants);
		context.checking(new Expectations() {{
			oneOf(asderListener).mobileChangedClothes(asder);
			oneOf(lollerListener).mobileChangedClothes(asder);
			oneOf(core).findContainerByContainedItem(pants);
				will(returnValue(null));
		}});
		
		asderSession.wearItemOnMobile(Layer.Pants, pants.getSerialId());
		
		assertEquals(pants, asder.getItemByLayer(Layer.Pants));
	}
	
	@Test
	public void wearingItemFromBackpackRemovesItFromBackpack() {
		context.checking(new Expectations() {{
			oneOf(asderListener).mobileChangedClothes(asder);
			oneOf(lollerListener).mobileChangedClothes(asder);
			
			oneOf(core).removeItemFromContainer(shoesWereInBackpack);
			oneOf(asderListener).containerChangedContents(backpack);
		}});
		
		assertTrue(backpack.getItems().contains(shoesWereInBackpack));
		assertNull(asder.getItemByLayer(Layer.Shoes));
		
		asderSession.wearItemOnMobile(Layer.Shoes, shoesWereInBackpack.getSerialId());
		
		assertFalse(backpack.getItems().contains(shoesWereInBackpack));
		assertEquals(shoesWereInBackpack, asder.getItemByLayer(Layer.Shoes));
	}
	
	@Test
	public void gettingOffClothAndPuttingItToBackpackRemovesItFromBodyAndAddsItToBackpack() {
		asder.setItemOnLayer(Layer.Pants, pants);

		context.checking(new Expectations() {{
			oneOf(core).findContainerByContainedItem(pants);
				will(returnValue(null));
			oneOf(core).addItemToContainer(pants, backpack);
				
			oneOf(asderListener).containerChangedContents(backpack);
			
			oneOf(asderListener).mobileDroppedCloth(asder, pants);
			oneOf(lollerListener).mobileDroppedCloth(asder, pants);
		}});

		assertFalse(backpack.getItems().contains(pants));
		assertEquals(pants, asder.getItemByLayer(Layer.Pants));

		asderSession.dropItem(pants.getSerialId(), false, backpack.getSerialId(), new PointInSpace(41, 42, 43));

		assertTrue(backpack.getItems().contains(pants));
		assertNull(asder.getItemByLayer(Layer.Pants));
	}
}
