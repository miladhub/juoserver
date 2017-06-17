package net.sf.juoserver.protocol;

public class LoginComplete extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x55;
	public LoginComplete() {
		super(CODE, 1);
	}
	@Override
	public int hashCode() {
		return 0;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
}
