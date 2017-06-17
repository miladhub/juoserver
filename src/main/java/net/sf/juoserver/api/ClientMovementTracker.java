package net.sf.juoserver.api;

/**
 * An object that keeps track of the client movements.
 */
public interface ClientMovementTracker {
	/**
	 * Updates the next sequence number we expect from the client.
	 */
	void incrementExpectedSequence();

	/**
	 * Returns the currently expected sequence.
	 * 
	 * @return the currently expected sequence
	 */
	int getExpectedSequence();
}