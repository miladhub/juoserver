package net.sf.juoserver.networking.threaded;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.juoserver.api.Configuration;
import net.sf.juoserver.api.Server;
import net.sf.juoserver.protocol.ProtocolIoPort;
import net.sf.juoserver.protocol.ControllerFactory;
import net.sf.juoserver.protocol.Huffman;
import net.sf.juoserver.protocol.UOProtocolMessageReader;

import org.apache.log4j.Logger;

public final class ThreadedServerAdapter implements Server {
	private static Logger logger = Logger.getLogger(ThreadedServerAdapter.class);
	private static final int FIRST_CLIENT_ID = 1;
	
	private final ClientThreadsManager clientThreadsManager = new ClientThreadsManager();
	private final List<ThreadedProtocolIoPort> clients = new CopyOnWriteArrayList<ThreadedProtocolIoPort>();
	private final Configuration configuration;
	private final ControllerFactory controllerFactory;

	private ServerSocket serverSocket;
	private volatile boolean active;
	private int nextClientId = FIRST_CLIENT_ID;
	
	public ThreadedServerAdapter(Configuration configuration, ControllerFactory controllerFactory) {
		super();
		this.configuration = configuration;
		this.controllerFactory = controllerFactory;
	}
	
	@Override
	public void acceptClientConnections() throws IOException {
		logger.info("Starting threaded server...");
		serverSocket = new ServerSocket(configuration.getServerPort());
		active = true;
		logger.info("Listening on port " + configuration.getServerPort());
		try {
			while (active) {
				Socket connection = waitForConnection();
				attachClient(createHandler(connection, "client-" + nextClientId++));
			}
		} finally {
			tearDown();
		}
	}

	private ThreadedProtocolIoPort createHandler(Socket connection, String clientName) throws IOException {
		return new ThreadedProtocolIoPort(clientName, this, controllerFactory, createWire(connection, clientName));
	}

	private UOProtocolWire createWire(Socket connection, String clientName) {
		return new UOProtocolWire(clientName, connection, new UOProtocolMessageReader(), new Huffman(), configuration);
	}

	private void tearDown() throws IOException {
		logger.info("Shutting server down...");
		active = false;
		if (serverSocket != null) {
			serverSocket.close();
		}
		for (ThreadedProtocolIoPort c : new ArrayList<ThreadedProtocolIoPort>( clients )) {
			shutDownClient(c);
		}
	}
	
	public void detachClient(ProtocolIoPort client) {
		if (clients.remove( client )) {
			logger.info(getClientsString());
		}
	}
	
	private void attachClient(final ThreadedProtocolIoPort client) throws IOException {
		if (clients.add( client )) {
			logger.info(getClientsString());
		}
		client.init();
		clientThreadsManager.startClientThread(client);
	}
	
	private void shutDownClient(ThreadedProtocolIoPort c) throws IOException {
		c.shutDown();
		clientThreadsManager.joinClientThread(c);
	}

	private Socket waitForConnection() throws IOException {
		return serverSocket.accept();
	}
	
	private String getClientsString() {
		return clients.size() + (clients.size() != 1? " clients" : " client") + " connected.";
	}
}
