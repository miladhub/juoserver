package net.sf.juoserver.model;

import static org.junit.Assert.*;
import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.Mobile;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CharacterSelectionTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	private final Core core = context.mock(Core.class);
	private final Account account = new UOAccount(42, "user", "psw");
	private final UOPlayerSession session = new UOPlayerSession(core, account, null, null);
	
	@Before
	public void setup() {
		account.addMobileSerialId(42);
	}
	
	@Test
	public void selectingCharacterCreatesMobile() {
		final Mobile oy = TestingFactory.createTestMobile(4, "Oy");
		
		context.checking(new Expectations() {{
			oneOf(core).findMobileByID(42);
				will(returnValue(oy));
		}});
		session.selectCharacterById(0);
		assertEquals(oy, session.getMobile());
	}
}
