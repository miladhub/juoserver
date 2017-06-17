package net.sf.juoserver.model;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.jmock.Expectations;
import org.junit.Test;


public class AttackComponentTest extends AbstractComponentTest {
	@Test
	public void testAttack() {
		context.checking(new Expectations() {{
			oneOf(lollerListener).mobileAttacked(asder);						
		}});
		asderSession.attack(loller);
		assertEquals(new HashSet<>(asList(asder)), lollerSession.attackingMe);
		assertEquals(loller, asderSession.attacking);
	}
	
	@Test
	public void testAttackFinished() {
		context.checking(new Expectations(){{
			oneOf(lollerListener).mobileAttacked(asder);			
			oneOf(asderListener).mobileChangedWarMode(asder);
			oneOf(lollerListener).mobileChangedWarMode(asder);			
			oneOf(asderListener).mobileAttackFinished(loller);
			oneOf(lollerListener).mobileAttackFinished(asder);
		}});
		asderSession.attack(loller);
		asderSession.toggleWarMode(false);
		assertNull(asderSession.attacking);
		assertTrue(lollerSession.attackingMe.isEmpty());
	}
	
	@Test
	public void testBidirectionalAttack() {
		context.checking(new Expectations(){{
			oneOf(lollerListener).mobileAttacked(asder);
			oneOf(asderListener).mobileAttacked(loller);			
		}});
		asderSession.attack(loller);
		lollerSession.attack(asder);
		assertEquals(loller, asderSession.attacking);
		assertEquals(asder, lollerSession.attacking);
		assertEquals(new HashSet<>(asList(asder)), lollerSession.attackingMe);
		assertEquals(new HashSet<>(asList(loller)), asderSession.attackingMe);
	}
	
	@Test
	public void testEndOfAnAttackBidirectional() {
		context.checking(new Expectations(){{
			oneOf(lollerListener).mobileAttacked(asder);
			oneOf(asderListener).mobileAttacked(loller);	
			
			oneOf(asderListener).mobileChangedWarMode(asder);
			oneOf(lollerListener).mobileChangedWarMode(asder);
		}});
		asderSession.attack(loller);
		lollerSession.attack(asder);
		asderSession.toggleWarMode(false);
		assertEquals(asder, lollerSession.attacking);
		assertEquals(new HashSet<>(asList(loller)), asderSession.attackingMe);
		assertNull(asderSession.attacking);
		assertTrue(lollerSession.attackingMe.isEmpty());
	}
	
	@Test
	public void testEndOfBidirectionalAttack() {
		context.checking(new Expectations(){{
			oneOf(lollerListener).mobileAttacked(asder);
			oneOf(asderListener).mobileAttacked(loller);	
			
			oneOf(asderListener).mobileChangedWarMode(asder);
			oneOf(lollerListener).mobileChangedWarMode(asder);
			
			oneOf(asderListener).mobileChangedWarMode(loller);
			oneOf(lollerListener).mobileChangedWarMode(loller);
			
			oneOf(asderListener).mobileAttackFinished(loller);
			oneOf(lollerListener).mobileAttackFinished(asder);
		}});
		asderSession.attack(loller);
		lollerSession.attack(asder);
		asderSession.toggleWarMode(false);
		lollerSession.toggleWarMode(false);
		assertNull(asderSession.attacking);
		assertNull(lollerSession.attacking);
		assertTrue(asderSession.attackingMe.isEmpty());
		assertTrue(lollerSession.attackingMe.isEmpty());
	}
}
