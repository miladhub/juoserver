package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;

@Decodable(code = PickUpItem.CODE)
public class PickUpItem extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x07;
	private int itemSerialId;
	private short amount;

	public PickUpItem(int itemSerialId, short amount) {
		this();
		this.itemSerialId = itemSerialId;
		this.amount = amount;
	}

	public PickUpItem(byte[] contents) {
		this();
		ByteBuffer bb = wrapContents(contents);
		itemSerialId = bb.getInt();
		amount = bb.getShort();
	}

	private PickUpItem() {
		super(CODE, 7);
	}
	
	public int getItemSerialId() {
		return itemSerialId;
	}

	public short getAmount() {
		return amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + itemSerialId;
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
		PickUpItem other = (PickUpItem) obj;
		if (amount != other.amount)
			return false;
		if (itemSerialId != other.itemSerialId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PickUpItem [itemSerialId=" + itemSerialId + ", amount="
				+ amount + "]";
	}
}
