package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.JUoEntity;

public class ObjectRevision extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0xDC;
	private static final int BASE_HASH = 0x40000000;
	
	private int serial;
	private int hash;

	private ObjectRevision(JUoEntity obj) {
		super(CODE, 9);
		this.serial = obj.getSerialId();
	}
	
	public ObjectRevision(Mobile mobile) {
		this((JUoEntity) mobile);
		this.hash = BASE_HASH + RevisionUtils.mobileRevisionHashCode(mobile);
	}

	public ObjectRevision(Item item) {
		this((JUoEntity) item);
		this.hash = 0x40000000 + RevisionUtils.itemRevisionHashCode(item);
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putInt(serial);
		bb.putInt(hash);
		return bb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hash;
		result = prime * result + serial;
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
		ObjectRevision other = (ObjectRevision) obj;
		if (hash != other.hash)
			return false;
		if (serial != other.serial)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjectRevision [serial=" + serial + ", hash=" + hash + "]";
	}
}
