package net.sf.juoserver.networking;

import java.util.Arrays;
import java.util.List;

import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.ProtocolController;
import net.sf.juoserver.api.ProtocolRouter;

public final class PriorityBasedProtocolRouter implements ProtocolRouter {
	private List<ProtocolController> controllers;
	
	public PriorityBasedProtocolRouter(ProtocolController... controllers) {
		super();
		this.controllers = Arrays.asList(controllers);
	}

	@Override
	public ProtocolController selectController(Message message) {
		for (ProtocolController controller : controllers) {
			if (controller.isInterestedIn(message)) {
				return controller;
			}
		}
		return controllers.get(controllers.size() - 1);
	}
}
