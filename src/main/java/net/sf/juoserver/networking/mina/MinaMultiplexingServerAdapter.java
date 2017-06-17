package net.sf.juoserver.networking.mina;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import net.sf.juoserver.api.Configuration;
import net.sf.juoserver.api.Server;
import net.sf.juoserver.protocol.ControllerFactory;

public class MinaMultiplexingServerAdapter implements Server {
	private final static Logger logger = Logger.getLogger(MinaMultiplexingServerAdapter.class);
	private static final int BUF_SIZE = 1024;

	private final Configuration configuration;
	private final ControllerFactory controllerFactory;
	
	public MinaMultiplexingServerAdapter(Configuration configuration, ControllerFactory controllerFactory) {
		super();
		this.configuration = configuration;
		this.controllerFactory = controllerFactory;
	}
	
	@Override
	public void acceptClientConnections() throws IOException {
		logger.info("Starting multiplexing server...");
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		if (configuration.isPacketLoggingEnabled()) {
			acceptor.getFilterChain().addLast("transport logger", new LoggingFilter());
		}
		acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new UOProtocolFactory()));
		if (configuration.isPacketLoggingEnabled()) {
			// Mina's LoggingFilter is bugged: https://issues.apache.org/jira/browse/DIRMINA-833
			acceptor.getFilterChain().addLast("packets logger", new UOProtocolLoggingFilter());
		}
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.getSessionConfig().setReadBufferSize(BUF_SIZE);
		acceptor.getSessionConfig().setReuseAddress(true);
		acceptor.setHandler(new UOIoHandler(acceptor, controllerFactory));
		acceptor.bind(new InetSocketAddress(configuration.getServerPort()));
		logger.info("Listening on port " + configuration.getServerPort());
	}
}
