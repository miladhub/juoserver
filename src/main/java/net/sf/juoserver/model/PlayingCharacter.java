package net.sf.juoserver.model;

import java.io.Serializable;

public class PlayingCharacter implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user, password;
	public PlayingCharacter(String user, String password) {
		super();
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
		PlayingCharacter other = (PlayingCharacter) obj;
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
		return "PlayingCharacter [password=" + password + ", user=" + user
				+ "]";
	}
}
