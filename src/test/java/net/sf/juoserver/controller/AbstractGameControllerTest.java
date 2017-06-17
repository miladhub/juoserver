package net.sf.juoserver.controller;

import java.io.IOException;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.InterClientNetwork;
import net.sf.juoserver.api.LoginManager;
import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.PlayerSession;
import net.sf.juoserver.model.UOAccount;
import net.sf.juoserver.protocol.ProtocolIoPort;
import net.sf.juoserver.protocol.CircularClientMovementTracker;
import net.sf.juoserver.protocol.GameController;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;

public class AbstractGameControllerTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	protected final Mobile mobile = TestingFactory.createTestMobile(42, "asder");
	protected final Account account = UOAccount.createAccount(0, "user", "psw", mobile.getSerialId());
	protected final PlayerSession session = context.mock(PlayerSession.class);
	
	protected final Core core = context.mock(Core.class);
	protected final LoginManager loginManager = context.mock(LoginManager.class);
	protected final InterClientNetwork intercom = context.mock(InterClientNetwork.class);
	protected final ProtocolIoPort clientHandler = context.mock(ProtocolIoPort.class);
	protected final GameController gameController = 
			new GameController("client", clientHandler, core, new CircularClientMovementTracker(), loginManager, intercom);
	
	@Before
	public final void createComponents() throws IOException {
		gameController.setSession(session);
		
		context.checking(new Expectations() {{
			allowing(session).getMobile();
				will(returnValue(mobile));
			allowing(core).findMobileByID(mobile.getSerialId());
				will(returnValue(mobile));
		}});
	}
}
