package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import net.sf.juoserver.model.ServerInfo;

import org.apache.commons.lang.ArrayUtils;

public class ServerList extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	public static final int CODE = 0xA8;
	public static final int SERVER_NAME_SIZE = 32;
	public static final byte UNKNOWN_BYTE = (byte) 0x5D;
	private List<ServerInfo> servers;
	public ServerList(ServerInfo... servers) {
		super(CODE, 6 + servers.length * (2 + SERVER_NAME_SIZE + 2 + 4));
		this.servers = Arrays.asList( servers );
	}
	public List<ServerInfo> getServers() {
		return servers;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.put(UNKNOWN_BYTE);
		bb.putShort((short) servers.size());
		short i = 0;
		for (ServerInfo server : servers) {
			bb.putShort(i++);
			bb.put( Arrays.copyOf(server.getName().getBytes(), SERVER_NAME_SIZE) );
			bb.put((byte) 0); // TODO: percent full
			bb.put((byte) 0); // TODO: time zone
			byte[] ipBytes = server.getAddress().getAddress();
			ArrayUtils.reverse( ipBytes );
			bb.put( Arrays.copyOf(ipBytes, 4) );
		}
		return bb;
	}
	@Override
	public boolean isCompressed() {
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((servers == null) ? 0 : servers.hashCode());
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
		ServerList other = (ServerList) obj;
		if (servers == null) {
			if (other.servers != null)
				return false;
		} else if (!servers.equals(other.servers))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [servers=" + servers + "]";
	}
}
