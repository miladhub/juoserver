package net.sf.juoserver.api;

import java.io.IOException;

public interface Server {
	void acceptClientConnections() throws IOException;
}
