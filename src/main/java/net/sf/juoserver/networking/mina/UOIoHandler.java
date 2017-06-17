package net.sf.juoserver.networking.mina;

import java.util.HashMap;
import java.util.Map;

import net.sf.juoserver.api.Message;
import net.sf.juoserver.protocol.ControllerFactory;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class UOIoHandler extends IoHandlerAdapter {
	private final Map<Long, MinaProtocolIoPortAdapter> handlers = new HashMap<Long, MinaProtocolIoPortAdapter>();
	private final ControllerFactory controllerFactory;
	private final IoAcceptor acceptor;
	
	public UOIoHandler(IoAcceptor acceptor, ControllerFactory controllerFactory) {
		super();
		this.acceptor = acceptor;
		this.controllerFactory = controllerFactory;
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		handlers.put(session.getId(), new MinaProtocolIoPortAdapter(session, controllerFactory));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		handlers.remove(session.getId());
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		handlers.get(session.getId()).handleClientMessage((Message) message);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		handlers.remove(session.getId());
		acceptor.unbind();
	}
}
