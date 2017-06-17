package net.sf.juoserver.networking.mina;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public class UOProtocolLoggingFilter extends IoFilterAdapter {
	private static final Logger logger = Logger.getLogger(UOProtocolLoggingFilter.class);
	
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		logger.info("Received message: " + message);
		nextFilter.messageReceived(session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		logger.info("Sending message: " + writeRequest.getMessage());
		nextFilter.messageSent(session, writeRequest);
	}
}
