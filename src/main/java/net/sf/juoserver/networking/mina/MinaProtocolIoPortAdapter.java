package net.sf.juoserver.networking.mina;

import java.io.IOException;

import net.sf.juoserver.api.Message;
import net.sf.juoserver.networking.AbstractProtocolIoPort;
import net.sf.juoserver.protocol.ControllerFactory;

import org.apache.mina.core.session.IoSession;

public class MinaProtocolIoPortAdapter extends AbstractProtocolIoPort {
	private final IoSession session;
	
	public MinaProtocolIoPortAdapter(IoSession session, ControllerFactory controllerFactory) {
		super("client-" + session.getId(), controllerFactory);
		this.session = session;
	}

	@Override
	public void sendToClient(Message... messages) throws IOException {
		for (Message message : messages) {
			session.write(message);
		}
	}

	@Override
	public void deactivate() {
		session.close(true);
	}
}
