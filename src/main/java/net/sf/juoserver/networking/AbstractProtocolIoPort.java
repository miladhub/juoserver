package net.sf.juoserver.networking;

import java.io.IOException;
import java.util.List;

import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.ProtocolController;
import net.sf.juoserver.api.ProtocolRouter;
import net.sf.juoserver.protocol.ProtocolIoPort;
import net.sf.juoserver.protocol.ControllerFactory;

public abstract class AbstractProtocolIoPort implements ProtocolIoPort {
	private final String clientName;
	private final ProtocolController authenticationController;
	private final ProtocolController gameController;
	private final ProtocolRouter router;
	
	public AbstractProtocolIoPort(String clientName, ControllerFactory controllerFactory) {
		super();
		this.clientName = clientName;
		authenticationController = controllerFactory.createAuthenticationController(this);
		gameController = controllerFactory.createGameController(this);
		router = new PriorityBasedProtocolRouter(authenticationController, gameController);
	}

	@Override
	public final void handleClientMessage(Message message) throws IOException {
		ProtocolController controllerInstance = router.selectController(message);
		List<Message> replies = controllerInstance.getReply(message);
		if (replies != null) {
			for (Message reply : replies) {
				sendToClient(reply);
			}
		}
		controllerInstance.postProcess(message);
	}

	@Override
	public String getName() {
		return clientName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientName == null) ? 0 : clientName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractProtocolIoPort other = (AbstractProtocolIoPort) obj;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		return true;
	}

	@Override
	public final String toString() {
		return clientName;
	}
}
