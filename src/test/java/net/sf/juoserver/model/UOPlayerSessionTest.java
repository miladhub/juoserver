package net.sf.juoserver.model;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.Direction;
import net.sf.juoserver.api.InterClientNetwork;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.files.mondainslegacy.MondainsLegacyMapTile;

public class UOPlayerSessionTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	private final Core core = context.mock(Core.class);
	private final ModelOutputPort listener = context.mock(ModelOutputPort.class, "asder listener");
	private final InterClientNetwork network = context.mock(InterClientNetwork.class);
	
	private final Account account = new UOAccount(41, "asder", "asder");
	private final Mobile asder = TestingFactory.createTestMobile(100, "asder");
	
	private final UOPlayerSession session = new UOPlayerSession(core, account, listener, network);
	
	@Before
	public void createMobiles() {
		context.checking(new Expectations() {{
			allowing(core).getTile(with(any(int.class)), with(any(int.class)));
				will(returnValue(new MondainsLegacyMapTile(0, 42)));
			allowing(core).findMobileByID(100);
				will(returnValue(asder));
		}});
		
		account.addMobileSerialId(100);
		session.selectCharacterById(0);
	}
	
	@Test
	public void mobileMovesForwardWhenMovesAccordingToDirection() {
		asder.setDirection(Direction.North);
		int previousY = asder.getY();
		
		context.checking(new Expectations() {{
			oneOf(network).notifyOtherMobileMovement(asder);
		}});
		
		session.move(Direction.North, false);
		
		assertEquals(previousY - 1, asder.getY());
		assertEquals(Direction.North, asder.getDirection());
	}
	
	@Test
	public void mobileDoesNotChangePositionWhenOnlyChangingDirection() {
		asder.setDirection(Direction.South);
		int previousY = asder.getY();
		
		context.checking(new Expectations() {{
			oneOf(network).notifyOtherMobileMovement(asder);
		}});
		
		session.move(Direction.North, false);
		
		assertEquals(previousY, asder.getY());
		assertEquals(Direction.North, asder.getDirection());
	}
	
	@Test
	public void mobileAttacks() {
		final Mobile loller = TestingFactory.createTestMobile(442, "loller");
		context.checking(new Expectations(){{
			oneOf(network).notifyAttacked(asder, loller);
		}});
		session.attack(loller);
	}
}
