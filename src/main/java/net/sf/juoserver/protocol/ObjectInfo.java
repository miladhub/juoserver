package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Item;

public class ObjectInfo extends AbstractMessage {
	private static final int CODE = 0x1A;
	private static final long serialVersionUID = 1L;
	private Item item;
	
	// TODO: put these into item
	private int x;
	private int y;
	private int z;

	public ObjectInfo(Item item, int x, int y, int z) {
		super(CODE, getLength(item));
		this.item = item;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	private static int getLength(Item item) {
		return 1 + 2 + 4 + 2 + 2 + 2 + 2 + 1 + 2 + 1;
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.putInt(item.getSerialId() | 0x80000000); // To make the amount be read
		bb.putShort((short) (item.getModelId() & 0x3FFF));
		bb.putShort((short) 1); // Amount info (TODO: move to Item)
		bb.putShort((short) (x & 0x7FFF)); // Never sending direction info
		bb.putShort((short) (y | 0x8000)); // Always sending hue info
		bb.put((byte) z);
		bb.putShort((short) item.getHue());
		bb.put((byte) 0x20); // Movable flag (TODO: item flags enum and move to Item)
		return bb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		ObjectInfo other = (ObjectInfo) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjectInfo [item=" + item + ", x=" + x + ", y=" + y + ", z="
				+ z + "]";
	}
}
