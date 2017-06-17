package net.sf.juoserver.model;

import static org.junit.Assert.*;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.Mobile;

import org.junit.Before;
import org.junit.Test;

public class MobileTest {

	private Mobile mob;
	private UOItem pants;
	
	@Before
	public void createMobile() {
		mob = TestingFactory.createTestMobile(42, "Test");
		pants = createPants();
		mob.setItemOnLayer(Layer.Pants, pants);
	}

	@Test
	public void getItemByLayer() {
		assertEquals(createPants(), mob.getItemByLayer(Layer.Pants));
	}
	
	@Test
	public void getLayer() {
		assertEquals(Layer.Pants, mob.getLayer(pants));
	}
	
	@Test
	public void removeItem() {
		mob.removeItem(pants);
		assertNull(mob.getItemByLayer(Layer.Pants));
	}
	
	@Test
	public void emptyLayer() {
		mob.emptyLayer(Layer.Pants);
		assertNull(mob.getItemByLayer(Layer.Pants));
	}
	
	@Test
	public void addItem() {
		mob.removeItem(pants);
		mob.setItemOnLayer(Layer.Pants, createPants());
		assertEquals(Layer.Pants, mob.getLayer(pants));
	}
	
	private UOItem createPants() {
		return new UOItem(100, 101, 4);
	}
	
}
