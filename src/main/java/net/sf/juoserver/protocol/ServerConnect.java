package net.sf.juoserver.protocol;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ServerConnect extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	public static final int CODE = 0x8C;
	private InetAddress host;
	private int port;
	private int authenticationKey;
	public ServerConnect(InetAddress host, int port, int authenticationKey) {
		super(CODE, 11);
		this.host = host;
		this.port = port;
		this.authenticationKey = authenticationKey;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put(host.getAddress());
		bb.putShort((short) port);
		bb.putInt(authenticationKey);
		return bb;
	}
	@Override
	public boolean isCompressed() {
		return false;
	}
	public InetAddress getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	public int getAuthenticationKey() {
		return authenticationKey;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authenticationKey;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
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
		ServerConnect other = (ServerConnect) obj;
		if (authenticationKey != other.authenticationKey)
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [authenticationKey=" + authenticationKey + ", host=" + host
				+ ", port=" + port + "]";
	}
}
