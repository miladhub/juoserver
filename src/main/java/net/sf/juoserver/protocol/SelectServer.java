package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;

@Decodable(code = SelectServer.CODE)
public class SelectServer extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0xA0;
	private int serverNumber;
	
	public SelectServer(byte[] contents) {
		super(CODE, 3);
		ByteBuffer bb = wrapContents(contents); // Parto dal secondo byte
		serverNumber = bb.getShort();
	}
	
	public SelectServer(int serverNumber) {
		super(CODE, 3);
		this.serverNumber = serverNumber;
	}

	public int getServerNumber() {
		return serverNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + serverNumber;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelectServer other = (SelectServer) obj;
		if (serverNumber != other.serverNumber)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [serverNumber=" + serverNumber + "]";
	}
}
