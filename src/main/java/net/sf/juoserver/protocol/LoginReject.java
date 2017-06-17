package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Coded;

public class LoginReject extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x53;
	private LoginRejectReasons reason;

	private LoginReject(LoginRejectReasons reason) {
		super(CODE, 2);
		this.reason = reason;
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put((byte) reason.getCode());
		return bb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
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
		LoginReject other = (LoginReject) obj;
		if (reason != other.reason)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "LoginReject [reason=" + reason + "]";
	}

	public static AbstractMessage loginRejectDueToBadPassword() {
		return new LoginReject(LoginRejectReasons.BadPassword);
	}

	public static AbstractMessage loginRejectDueToNoSuchCharacter() {
		return new LoginReject(LoginRejectReasons.NoSuchCharacter);
	}

	private static enum LoginRejectReasons implements Coded {
		BadPassword, NoSuchCharacter, CharAlreadyLoggedIn;

		@Override
		public int getCode() {
			return ordinal();
		}
	}
}
