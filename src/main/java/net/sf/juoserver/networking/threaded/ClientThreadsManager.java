package net.sf.juoserver.networking.threaded;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.juoserver.protocol.ProtocolIoPort;

public class ClientThreadsManager {
	private static final Logger logger = Logger.getLogger(ClientThreadsManager.class);
	
	/**
	 * Client threads, indexed by {@link ProtocolIoPort#getName()}.
	 */
	private final Map<String, Thread> clientThreads = new HashMap<String, Thread>();

	public void joinClientThread(ProtocolIoPort c) {
		try {
			clientThreads.get(c.getName()).join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Client thread was interrupted", e);
		}
	}

	public void startClientThread(final ThreadedProtocolIoPort client) {
		Thread t = new Thread(wrapClientAsRunnable(client), client.getName());
		clientThreads.put(client.getName(), t);
		t.start();
	}

	private Runnable wrapClientAsRunnable(final ThreadedProtocolIoPort client) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					client.startUp();
				} catch (IOException e) {
					logger.error("I/O error occurred", e);
				} finally {
					try {
						client.shutDown();
					} catch (IOException e) {
						logger.error("I/O error on shutdown!", e);
					}
				}
			}
		};
	}
}
