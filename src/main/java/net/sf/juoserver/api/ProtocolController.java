package net.sf.juoserver.api;

import java.util.List;

/**
 * Contract for a protocol controller.
 * <p/>
 * A protocol controller is an object whose responsibility is to decide what
 * message to reply with basing the decision on the request message.
 * <p/>
 * In order not to let the communication details slip into the model entities,
 * the protocol controller should take care of all of the protocol mechanisms
 * and of them only, delegating all the rest to the model - e.g., it will handle
 * the message requesting a double-click to use an item, but will not directly
 * execute the item usage - it will rather delegate it to the model.
 * <p/>
 * The protocol controller is defined assuming a basic request-reply
 * communication pattern.
 */
public interface ProtocolController {
	/**
	 * Retrieves the reply for a certain request message.
	 * 
	 * @param msg request message
	 * @return reply message
	 */
	List<Message> getReply(Message msg);

	/**
	 * Post-processes the request message.
	 * 
	 * @param request the request message to be post-processed
	 */
	void postProcess(Message request);

	boolean isInterestedIn(Message message);
}