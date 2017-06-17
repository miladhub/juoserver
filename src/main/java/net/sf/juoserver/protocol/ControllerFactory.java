package net.sf.juoserver.protocol;

import net.sf.juoserver.api.Configuration;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.InterClientNetwork;
import net.sf.juoserver.api.LoginManager;
import net.sf.juoserver.api.ProtocolController;
import net.sf.juoserver.model.Intercom;
import net.sf.juoserver.model.UOLoginManager;

public final class ControllerFactory {
	private final Core core;
	private final Configuration configuration;
	private final LoginManager loginManager;
	private final InterClientNetwork network;
	
	public ControllerFactory(Core core, Configuration configuration) {
		super();
		this.core = core;
		this.configuration = configuration;
		loginManager = new UOLoginManager(core);
		network = new Intercom();
	}

	public ProtocolController createGameController(ProtocolIoPort clientHandler) {
		return new GameController(clientHandler.getName(), clientHandler, core, new CircularClientMovementTracker(),
				loginManager, network);
	}
	
	public ProtocolController createAuthenticationController(ProtocolIoPort clientHandler) {
		return new AuthenticationController(clientHandler, configuration, loginManager);
	}
}
