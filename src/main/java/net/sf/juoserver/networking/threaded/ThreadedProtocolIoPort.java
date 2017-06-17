package net.sf.juoserver.networking.threaded;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import net.sf.juoserver.api.Message;
import net.sf.juoserver.networking.AbstractProtocolIoPort;
import net.sf.juoserver.protocol.ControllerFactory;

public class ThreadedProtocolIoPort extends AbstractProtocolIoPort {
	private final ThreadedServerAdapter server;
	private final MessageWire wire;

	private volatile boolean active;

	public ThreadedProtocolIoPort(String name, ThreadedServerAdapter server, ControllerFactory controllerFactory,
			MessageWire wire) throws IOException {
		super(name, controllerFactory);
		this.server = server;
		this.wire = wire;
	}
	
	@Override
	public void sendToClient(Message... messages) throws IOException {
		wire.sendMessages(Arrays.asList(messages));
	}
	
	public void init() throws IOException {
		wire.init();
		active = true;
	}

	public void startUp() throws IOException {
		while (active) {
			List<? extends Message> messages = wire.readMessages();
			if (messages == null) {
				break;
			}
			for (Message msg : messages) {
				handleClientMessage(msg);
			}
		}
	}
	
	@Override
	public void deactivate() {
		this.active = false;
	}
	
	public final void shutDown() throws IOException {
		active = false;
		wire.shutDown();
		server.detachClient( this );
	}
}
