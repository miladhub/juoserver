package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;

@Decodable(code = ServerLoginRequest.CODE)
public class ServerLoginRequest extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x91;
	private int authenticationKey;
	private String user;
	private String password;
	public ServerLoginRequest(byte[] contents) {
		super(CODE, 65);
		ByteBuffer bb = wrapContents(contents);
		authenticationKey = bb.getInt();
		user = MessagesUtils.readString(bb, 30);
		password = MessagesUtils.readString(bb, 30);
	}
	
	public ServerLoginRequest(int authenticationKey, String user, String password) {
		super(CODE, 65);
		this.authenticationKey = authenticationKey;
		this.user = user;
		this.password = password;
	}

	public int getAuthenticationKey() {
		return authenticationKey;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authenticationKey;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		ServerLoginRequest other = (ServerLoginRequest) obj;
		if (authenticationKey != other.authenticationKey)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [key=" + authenticationKey + ", password=" + password
				+ ", user=" + user + "]";
	}
}
