package net.sf.juoserver.main;

import java.io.IOException;

import net.sf.juoserver.api.Configuration;
import net.sf.juoserver.api.Core;
import net.sf.juoserver.api.Server;
import net.sf.juoserver.configuration.PropertyFileBasedConfiguration;
import net.sf.juoserver.files.mondainslegacy.MondainsLegacyFileReadersFactory;
import net.sf.juoserver.model.UOCore;
import net.sf.juoserver.model.InMemoryDataManager;
import net.sf.juoserver.networking.mina.MinaMultiplexingServerAdapter;
import net.sf.juoserver.networking.threaded.ThreadedServerAdapter;
import net.sf.juoserver.protocol.ControllerFactory;

public class Main {
	private static enum ServerType { THREADED, MULTIPLEXING };
	private final Configuration configuration;
	private final Core core;
	private final Server server;

	private Main(ServerType serverType) {
		super();
		configuration = new PropertyFileBasedConfiguration();
		core = new UOCore(new MondainsLegacyFileReadersFactory(), new InMemoryDataManager(), configuration);
		server = createServer(serverType, core, configuration);
	}
	
	private void run() throws IOException {
		core.init();
		server.acceptClientConnections();
	}
	
	private Server createServer(ServerType serverType, Core core, Configuration configuration) {
		switch (serverType) {
		case MULTIPLEXING:
			return new MinaMultiplexingServerAdapter(configuration, new ControllerFactory(core, configuration));
		case THREADED:
			return new ThreadedServerAdapter(configuration, new ControllerFactory(core, configuration));
		default:
			throw new IllegalStateException(serverType.name());
		}
	}

	public static void main(String[] args) throws IOException {
		new Main(parseServerType(args)).run();
	}
	
	private static ServerType parseServerType(String[] args) {
		return args.length == 0? ServerType.MULTIPLEXING : ServerType.valueOf(args[0].toUpperCase());
	}
}
