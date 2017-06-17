package net.sf.juoserver.protocol;

import net.sf.juoserver.api.Decodable;

/**
 * Login request message, sent by the client.
 * @see <a href="http://kec.cz/tartaros/steamengine/uploads/SE%20packet%20guide/www.twilightmanor.net/se/packetsdf12.html?id=29&style=gold">packet guide</a>
 */
@Decodable(code = LoginRequest.CODE)
public class LoginRequest extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x80;
	
	private String user;
	private String password;
	
	public LoginRequest(byte[] contents) {
		super(CODE, 62); // Last byte is not used
		user = MessagesUtils.getString(contents, 1, 30);
		password = MessagesUtils.getString(contents, 31, 60);
	}
	
	public LoginRequest(String user, String password) {
		super(CODE, 62);
		this.user = user;
		this.password = password;
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
		LoginRequest other = (LoginRequest) obj;
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
		return getName() + " [user=" + user + ", password=" + password + "]";
	}
}
