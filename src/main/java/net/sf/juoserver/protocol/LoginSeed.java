package net.sf.juoserver.protocol;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.ArrayUtils;

/**
 * First message ever sent by a client.
 * It's made up of 4 bytes, usually consisting
 * of the client's IP address.
 */
public class LoginSeed extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	public static final int CODE = NO_CODE;
	private InetAddress address;
	
	private LoginSeed() {
		super(CODE, 4);
	}

	public LoginSeed(byte[] contents) throws UnknownHostException {
		this();
		address = InetAddress.getByAddress(ArrayUtils.subarray(contents, 0, getLength()));
	}
	
	public LoginSeed(InetAddress address) {
		this();
		this.address = address;
	}

	public InetAddress getAddress() {
		return address;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
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
		LoginSeed other = (LoginSeed) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [address=" + address + "]";
	}
}
