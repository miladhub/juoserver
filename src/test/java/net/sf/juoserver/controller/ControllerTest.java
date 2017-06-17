package net.sf.juoserver.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.CharacterStatus;
import net.sf.juoserver.api.LightLevels;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.PlayerSession;
import net.sf.juoserver.api.Season;
import net.sf.juoserver.model.UOGameStatus;
import net.sf.juoserver.protocol.CharacterList;
import net.sf.juoserver.protocol.CharacterSelect;
import net.sf.juoserver.protocol.ClientVersion;
import net.sf.juoserver.protocol.ClilocMessage;
import net.sf.juoserver.protocol.LookRequest;
import net.sf.juoserver.protocol.MegaClilocRequest;
import net.sf.juoserver.protocol.MegaClilocResponse;
import net.sf.juoserver.protocol.ServerLoginRequest;
import net.sf.juoserver.protocol.WarMode;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.Expectations;
import org.junit.Test;

public class ControllerTest extends AbstractGameControllerTest {
	@Test
	public void selectServerGeneratesAuthKeyUsedToRetrieveAccountOnServerLoginRequest() throws IOException {
		final int secretKey = 12345678;
		context.checking(new Expectations() {{
			oneOf(loginManager).getAuthorizedAccount(secretKey);
				will(returnValue(account));
			oneOf(intercom).addIntercomListener(with(aNewSession()));
		}});
		
		CharacterList chars = gameController.handle(new ServerLoginRequest(secretKey, "user", "psw"));

		assertEquals(mobile.getName(), chars.getPcs().get(0).getUser());
	}

	private Matcher<PlayerSession> aNewSession() {
		return new TypeSafeMatcher<PlayerSession>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("a new session");
			}

			@Override
			protected boolean matchesSafely(PlayerSession session) {
				return session.getMobile() == null;
			}
		};
	}
	
	@Test
	public void characterSelect() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).selectCharacterById(0);
		}});
		
		assertEquals(new ClientVersion(), gameController.handle( new CharacterSelect(mobile.getName(), 0)));
	}
	
	@Test
	public void receivingClientVersionStartsTheGame() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).startGame();
				will(returnValue(new UOGameStatus(LightLevels.OsiNight, Season.Winter)));
		}});
		
		gameController.handle( new ClientVersion("42") );
 	}
	
	@Test
	public void receivingClientVersionTwiceHasNoEffect() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).startGame();
				will(returnValue(new UOGameStatus(LightLevels.OsiNight, Season.Winter)));
		}});
		
		gameController.handle( new ClientVersion("42") );
		gameController.handle( new ClientVersion("42") );
 	}
	
	@Test
	public void singleClickOnMobile() throws IOException {
		final Mobile loller = TestingFactory.createTestMobile(442, "loller");
		context.checking(new Expectations() {{
			oneOf(core).findMobileByID(442);
				will(returnValue(loller));
		}});
		assertEquals(new ClilocMessage(loller), gameController.handle(new LookRequest(loller)));
	}
	
	@Test
	public void megaClilocOnMobile() throws IOException {
		final Mobile loller = TestingFactory.createTestMobile(442, "loller");
		context.checking(new Expectations() {{
			oneOf(core).findMobileByID(442);
				will(returnValue(loller));
		}});
		assertEquals(Arrays.asList(MegaClilocResponse.createMobileMegaClilocResponse(loller)),
				gameController.handle(new MegaClilocRequest(loller)));
	}
	
	@Test
	public void warMode() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).toggleWarMode(true);
		}});
		
		gameController.handle(new WarMode(CharacterStatus.WarMode));
	}
}
