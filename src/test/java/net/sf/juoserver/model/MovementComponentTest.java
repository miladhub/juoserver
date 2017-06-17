package net.sf.juoserver.model;

import java.io.IOException;

import net.sf.juoserver.api.Direction;

import org.jmock.Expectations;
import org.junit.Test;

public class MovementComponentTest extends AbstractComponentTest {
	@Test
	public void firstStepInRangeRedrawsBothMobsAndUpdatesMovingMobToOtherClients() throws IOException {
		context.checking(new Expectations() {{			
			oneOf(lollerListener).mobileApproached(asder);
			oneOf(asderListener).mobileApproached(loller);
			
			oneOf(lollerListener).mobileChanged(asder);
		}});
		
		asderSession.move(Direction.West, false);
	}
	
	@Test
	public void secondStepWithinRangeUpdatesMovingMobToOtherClientsOnly() throws IOException {
		context.checking(new Expectations() {{
			oneOf(lollerListener).mobileApproached(asder);
			oneOf(asderListener).mobileApproached(loller);
			
			exactly(2).of(lollerListener).mobileChanged(asder);
		}});
		
		asderSession.move(Direction.West, false);
		asderSession.move(Direction.West, false);
	}
}
