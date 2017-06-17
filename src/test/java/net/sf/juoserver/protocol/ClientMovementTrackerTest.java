package net.sf.juoserver.protocol;

import static org.junit.Assert.*;

import net.sf.juoserver.protocol.CircularClientMovementTracker;

import org.junit.Before;
import org.junit.Test;

public class ClientMovementTrackerTest {

	private CircularClientMovementTracker tracker;
	
	@Before
	public void createTracker() {
		tracker = new CircularClientMovementTracker();
	}
	
	@Test
	public void firstValueIsZero() {
		assertEquals(0, tracker.getExpectedSequence());
	}
	
	@Test
	public void anyValueGetIncremented() {
		tracker.setSequence(42);
		tracker.incrementExpectedSequence();
		assertEquals(43, tracker.getExpectedSequence());
	}
	
	@Test
	public void restartsFromOne() {
		tracker.setSequence(255);
		tracker.incrementExpectedSequence();
		assertEquals(1, tracker.getExpectedSequence());
	}
	
}
