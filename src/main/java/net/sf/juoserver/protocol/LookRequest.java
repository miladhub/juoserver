package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Mobile;

@Decodable(code = LookRequest.CODE)
public class LookRequest extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x09;
	
	/**
	 * Serial ID of the object (item or mobile) that the
	 * client wants to look at.
	 */
	private int serialId;

	private LookRequest() {
		super(CODE, 5);
	}
	
	public LookRequest(byte[] contents) {
		this();
		ByteBuffer bb = wrapContents(contents);
		serialId = bb.getInt();
	}

	public LookRequest(Mobile mobile) {
		this(mobile.getSerialId());
	}
	
	public LookRequest(int serialId) {
		this();
		this.serialId = serialId;
	}

	public int getSerialId() {
		return serialId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + serialId;
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
		LookRequest other = (LookRequest) obj;
		if (serialId != other.serialId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LookRequest [serialId=" + serialId + "]";
	}
}
