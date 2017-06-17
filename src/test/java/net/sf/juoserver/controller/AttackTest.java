package net.sf.juoserver.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.protocol.AttackOK;
import net.sf.juoserver.protocol.AttackRequest;
import net.sf.juoserver.protocol.AttackSucceed;
import net.sf.juoserver.protocol.FightOccurring;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;

public class AttackTest extends AbstractGameControllerTest {
	private Mobile loller;

	@Before
	public final void createMobiles() {
		loller = TestingFactory.createTestMobile(442, "loller");
	}

	@Test
	public void attackRequest() {
		context.checking(new Expectations() {{
				oneOf(core).findMobileByID(442);
				will(returnValue(loller));
				oneOf(session).attack(loller);
		}});
		assertEquals(Arrays.asList(new AttackOK(loller), new FightOccurring(
				mobile, loller), new AttackSucceed(loller)),
				gameController.handle(new AttackRequest(loller)));
	}
}
