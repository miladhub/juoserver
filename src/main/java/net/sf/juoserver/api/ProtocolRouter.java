package net.sf.juoserver.api;

public interface ProtocolRouter {
	ProtocolController selectController(Message msg);
}
