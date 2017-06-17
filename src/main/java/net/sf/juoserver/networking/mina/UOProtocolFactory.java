package net.sf.juoserver.networking.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class UOProtocolFactory implements ProtocolCodecFactory {
	private final ProtocolEncoder encoder = new UOProtocolEncoder();
	private final ProtocolDecoder decoder = new UOProtocolDecoder();

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}
}
