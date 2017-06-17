package net.sf.juoserver.model;

import net.sf.juoserver.TestingFactory;
import net.sf.juoserver.api.Account;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.InterClientNetwork;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.files.mondainslegacy.MondainsLegacyMapTile;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;

public class AbstractComponentTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	protected final Core core = context.mock(Core.class);
	protected final ModelOutputPort asderListener = context.mock(ModelOutputPort.class, "asder listener");
	protected final ModelOutputPort lollerListener = context.mock(ModelOutputPort.class, "loller listener");
	protected final Account asderAcct = new UOAccount(41, "asder", "asder");
	protected final Account lollerAcct = new UOAccount(42, "loller", "loller");
	protected final Mobile asder = TestingFactory.createTestMobile(100, "asder");
	protected final Mobile loller = TestingFactory.createTestMobile(101, "loller");
	protected final InterClientNetwork network = new Intercom(); 
	protected final UOPlayerSession asderSession = new UOPlayerSession(core, asderAcct, asderListener, network);
	protected final UOPlayerSession lollerSession = new UOPlayerSession(core, lollerAcct, lollerListener, network);
	
	@Before
	public void createNetworkAndSelectMobiles() {
		network.addIntercomListener(asderSession);
		network.addIntercomListener(lollerSession);
		
		context.checking(new Expectations() {{
			allowing(core).getTile(with(any(int.class)), with(any(int.class)));
				will(returnValue(new MondainsLegacyMapTile(0, 42)));
			allowing(core).findMobileByID(100);
				will(returnValue(asder));
			allowing(core).findMobileByID(101);
				will(returnValue(loller));
		}});
		
		asderAcct.addMobileSerialId(100);
		lollerAcct.addMobileSerialId(101);
		
		asderSession.selectCharacterById(0);
		lollerSession.selectCharacterById(0);
	}
}
