package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;

/**
 * Client version request/response. This message
 * can be sent by both client and server.
 */
@Decodable(code = ClientVersion.CODE)
public class ClientVersion extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0xBD;
	private String clientVersion;
	// ============ sent by the server ============
	public ClientVersion() {
		super(CODE, 3); // When sent by the server, length is always 3
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		return bb;
	}
	// ============ sent by the client ============
	public ClientVersion(byte[] contents) {
		super(CODE, MessagesUtils.getLengthFromSecondAndThirdByte(contents));
		clientVersion = MessagesUtils.getString(contents, 2, getLength() - 1).replaceAll("\n", "");
	}
	public ClientVersion(String clientVersion) {
		super(CODE, clientVersion.length() + 1);
		this.clientVersion = clientVersion;
	}
	public String getClientVersion() {
		return clientVersion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientVersion == null) ? 0 : clientVersion.hashCode());
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
		ClientVersion other = (ClientVersion) obj;
		if (clientVersion == null) {
			if (other.clientVersion != null)
				return false;
		} else if (!clientVersion.equals(other.clientVersion))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + (clientVersion != null? " [clientVersion=" + clientVersion + "]" : "");
	}
}
