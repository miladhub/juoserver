package net.sf.juoserver.protocol;

import net.sf.juoserver.api.ClientMovementTracker;

public class CircularClientMovementTracker implements ClientMovementTracker {
	private final int limit;
	private int sequence;
	
	public CircularClientMovementTracker() {
		super();
		this.sequence = 0;
		this.limit = 256;
	}
	
	@Override
	public int getExpectedSequence() {
		return sequence;
	}
	
	@Override
	public void incrementExpectedSequence() {
		sequence++;
		if (sequence == limit) {
			sequence = 1;
		}
	}

	void setSequence(int sequence) {
		this.sequence = sequence;
	}
}