package net.sf.juoserver.model;

import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.LoginManager;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class LoginManagerTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	private final Core core = context.mock(Core.class);
	private final Account account = new UOAccount(42, "user", "psw");
	private final LoginManager loginManager = new UOLoginManager(core);
	
	@Test
	public void successfulLoginAuthenticatesAccount() {
		context.checking(new Expectations() {{
			oneOf(core).findAccountByUsername("user");
				will(returnValue(account));
			oneOf(core).authenticate(account, "psw");
				will(returnValue(true));
		}});
		loginManager.login("user", "psw");
	}
}
