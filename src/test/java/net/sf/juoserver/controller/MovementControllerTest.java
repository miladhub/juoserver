package net.sf.juoserver.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import net.sf.juoserver.api.Direction;
import net.sf.juoserver.protocol.MoveRequest;
import net.sf.juoserver.protocol.MovementAck;
import net.sf.juoserver.protocol.MovementReject;

import org.jmock.Expectations;
import org.junit.Test;

public class MovementControllerTest extends AbstractGameControllerTest {
	@Test
	public void moveRequestBadSequence() throws IOException {
		assertEquals( Arrays.asList( new MovementReject(42, mobile.getX(), mobile.getY(), mobile.getZ(),
				mobile.getDirectionWithRunningInfo()) ), gameController.handle( new MoveRequest(Direction.West, 42, 99, false) ));
	}
	
	@Test
	public void moveRequestFirstStepInRangeIsAckowledged() throws IOException {
		context.checking(new Expectations() {{
			oneOf(session).move(Direction.West, false);
		}});
		
		assertEquals(Arrays.asList(new MovementAck(0, mobile.getNotoriety())),
				gameController.handle(new MoveRequest(Direction.West, 0, 99, false)));
	}
}
