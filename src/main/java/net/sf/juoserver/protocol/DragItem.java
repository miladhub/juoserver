package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;

public class DragItem extends AbstractMessage {
	private static final long serialVersionUID = 1L;

	private static final int CODE = 0x23;

	private Item item;
	private int itemAmount;
	private Mobile source;
	private int targetSerial;
	private int targetX;
	private int targetY;
	private int targetZ;
	
	public DragItem(Item item, int itemAmount,
			Mobile source, int targetSerial, int targetX, int targetY, int targetZ) {
		super(CODE, 26);
		this.item = item;
		this.itemAmount = itemAmount;
		this.source = source;
		this.targetSerial = targetSerial;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) item.getModelId());
		bb.put((byte) 0);
		bb.putShort((short) item.getHue()); // TODO: probably useless
		bb.putShort((short) itemAmount); // TODO: move this information to Item
		bb.putInt(source.getSerialId());
		bb.putShort((short) source.getX());
		bb.putShort((short) source.getY());
		bb.put((byte) source.getZ());
		bb.putInt(targetSerial); // Parent, zero represent no parent - TODO: this is going to have to be changeable
		bb.putShort((short) targetX);
		bb.putShort((short) targetY);
		bb.put((byte) targetZ);
		return bb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + itemAmount;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + targetSerial;
		result = prime * result + targetX;
		result = prime * result + targetY;
		result = prime * result + targetZ;
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
		DragItem other = (DragItem) obj;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (itemAmount != other.itemAmount)
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (targetSerial != other.targetSerial)
			return false;
		if (targetX != other.targetX)
			return false;
		if (targetY != other.targetY)
			return false;
		if (targetZ != other.targetZ)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DragItem [item=" + item + ", itemAmount=" + itemAmount
				+ ", source=" + source + ", targetSerial=" + targetSerial
				+ ", targetX=" + targetX + ", targetY=" + targetY
				+ ", targetZ=" + targetZ + "]";
	}
}
