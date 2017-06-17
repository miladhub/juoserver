package net.sf.juoserver.networking.threaded;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import net.sf.juoserver.api.Configuration;
import net.sf.juoserver.api.Encoder;
import net.sf.juoserver.api.Message;
import net.sf.juoserver.api.MessageReader;

public class UOProtocolWire implements MessageWire {
	private static final Logger logger = Logger.getLogger(UOProtocolWire.class);
	private static final int BUF_SIZE = 1024;

	private final String clientName;
	private final Socket socket;
	private final MessageReader messageReader;
	private final Encoder compressor;
	private final Configuration configuration;
	
	private InetAddress clientAddress;
	private InputStream is;
	private OutputStream os;
	private byte[] buffer = new byte[BUF_SIZE];

	public UOProtocolWire(String clientName, Socket socket, MessageReader messageReader,
			Encoder compressor, Configuration configuration) {
		this.clientName = clientName;
		this.socket = socket;
		this.messageReader = messageReader;
		this.compressor = compressor;
		this.configuration = configuration;
	}

	@Override
	public void init() throws IOException {
		clientAddress = socket.getInetAddress();
		is = socket.getInputStream();
		os = socket.getOutputStream();
		logger.info(clientName + " connected from " + clientAddress);
	}

	@Override
	public void shutDown() throws IOException {
		logger.info(clientName + " disconnected from "  + clientAddress);
		if (is != null) {
			is.close();
		}
		if (os != null) {
			os.close();
		}
		if (socket != null) {
			socket.close();
		}
	}

	@Override
	public void sendMessages(List<Message> messages) throws IOException {
		for (Message reply : messages) {
			if (configuration.isPacketLoggingEnabled()) {
				logger.info("Sending message: " + reply);
			}
			if (reply.isCompressed()) {
				os.write(compressor.encode(reply.encode().array()));
			} else {
				os.write(reply.encode().array());
			}
		}
	}

	@Override
	public List<? extends Message> readMessages() throws IOException {
		int nread = is.read(buffer, 0, BUF_SIZE);
		if (nread != -1) {
			byte[] inputBytes = Arrays.copyOf(buffer, nread);
			if (configuration.isPacketLoggingEnabled()) {
				logger.info("Received bytes: " + Hex.encodeHexString(inputBytes).toUpperCase());
			}
			List<Message> readMessages = messageReader.readMessages(inputBytes);
			if (configuration.isPacketLoggingEnabled()) {
				for (Message message : readMessages) {
					logger.info("Received message: " + message);
				}
			}
			return readMessages;
		} else {
			return null;
		}
	}
}
