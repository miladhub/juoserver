package net.sf.juoserver.protocol;

import net.sf.juoserver.api.Decodable;

@Decodable(code=DoubleClick.CODE)
public class DoubleClick extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x06;
	
	private int objectSerialId;
	private boolean paperdollRequest;
	
	public DoubleClick(byte[] contents) {
		super(CODE, 5);
		int rawSerialId = wrapContents(contents).getInt();
		paperdollRequest = (rawSerialId & 0x80000000) != 0;
		objectSerialId = rawSerialId & 0x7FFFFFFF;
	}
	
	public DoubleClick(int objectSerialId, boolean paperdollRequest) {
		super(CODE, 5);
		this.objectSerialId = objectSerialId;
		this.paperdollRequest = paperdollRequest;
	}

	public int getObjectSerialId() {
		return objectSerialId;
	}
	public boolean isPaperdollRequest() {
		return paperdollRequest;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + objectSerialId;
		result = prime * result + (paperdollRequest ? 1231 : 1237);
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
		DoubleClick other = (DoubleClick) obj;
		if (objectSerialId != other.objectSerialId)
			return false;
		if (paperdollRequest != other.paperdollRequest)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DoubleClick [objectSerialId=" + objectSerialId
				+ ", paperdollRequest=" + paperdollRequest + "]";
	}
}
