package net.sf.juoserver.controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.LoginManager;
import net.sf.juoserver.model.UOAccount;
import net.sf.juoserver.protocol.AuthenticationController;
import net.sf.juoserver.protocol.ProtocolIoPort;
import net.sf.juoserver.protocol.LoginRequest;
import net.sf.juoserver.protocol.LoginSeed;
import net.sf.juoserver.protocol.SelectServer;
import net.sf.juoserver.protocol.ServerConnect;

import org.apache.commons.codec.DecoderException;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class AuthenticationControllerTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();

	private final LoginManager loginManager = context.mock(LoginManager.class);
	private ProtocolIoPort clientHandler = context.mock(ProtocolIoPort.class);

	private final AuthenticationController controller = new AuthenticationController(clientHandler,
			TestingFactory.createTestConfiguration(), loginManager);
	
	@Test
	public void seedIsIgnored() throws UnknownHostException, IOException, DecoderException {
		controller.handle( new LoginSeed( InetAddress.getLocalHost() ) );
	}
	
	@Test
	public void login() throws IOException {
		context.checking(new Expectations() {{
			oneOf(loginManager).login("myuser", "mypsw");
		}});
		
		controller.handle( new LoginRequest("myuser", "mypsw"));
	}

	@Test
	public void selectServerGeneratesAuthKeyUsedToRetrieveSessionOnServerLoginRequest() throws IOException {
		final Account account = new UOAccount(42, "myuser", "mypsw");
		
		context.checking(new Expectations() {{
			oneOf(loginManager).login("myuser", "mypsw");
				will(returnValue(account));
			oneOf(loginManager).generateAuthenticationKey(account);
				will(returnValue(12345678));
		}});
		
		controller.handle( new LoginRequest("myuser", "mypsw"));
		ServerConnect message = (ServerConnect) controller.handle( new SelectServer(12) );
		assertEquals(12345678, message.getAuthenticationKey());
	}
}
