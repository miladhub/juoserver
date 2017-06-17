package net.sf.juoserver.networking.mina;

import net.sf.juoserver.api.Message;
import net.sf.juoserver.protocol.Huffman;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class UOProtocolEncoder extends ProtocolEncoderAdapter {
	private final Huffman compressor = new Huffman();
	
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Message uoMessage = (Message) message;
		byte[] encoded = encodeMessage(uoMessage);
		IoBuffer buffer = wrapAsIoBuffer(encoded);
		out.write(buffer);
	}

	private byte[] encodeMessage(Message uoMessage) {
		if (uoMessage.isCompressed()) {
			return compressor.encode(uoMessage.encode().array());
		} else {
			return uoMessage.encode().array();
		}
	}
	
	private IoBuffer wrapAsIoBuffer(byte[] encoded) {
		IoBuffer buffer = IoBuffer.allocate(encoded.length, true);
		buffer.put(encoded);
		buffer.flip();
		return buffer;
	}
}