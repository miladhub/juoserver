package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;

@Decodable(code = WearItem.CODE)
public class WearItem extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x13;
	private int itemSerialId;
	private int playerSerialId;
	private Layer layer;

	private WearItem() {
		super(CODE, 10);
	}
	
	public WearItem(Item item, Mobile mobile, Layer layer) {
		this();
		this.itemSerialId = item.getSerialId();
		this.playerSerialId = mobile.getSerialId();
		this.layer = layer;
	}

	public WearItem(byte[] contents) {
		this();
		ByteBuffer bb = wrapContents(contents);
		itemSerialId = bb.getInt();
		layer = Layer.byCode(bb.get());
		playerSerialId = bb.getInt();
	}

	public int getItemSerialId() {
		return itemSerialId;
	}

	public int getPlayerSerialId() {
		return playerSerialId;
	}

	public Layer getLayer() {
		return layer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + itemSerialId;
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		result = prime * result + playerSerialId;
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
		WearItem other = (WearItem) obj;
		if (itemSerialId != other.itemSerialId)
			return false;
		if (layer != other.layer)
			return false;
		if (playerSerialId != other.playerSerialId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WearItem [itemSerialId=" + itemSerialId + ", playerSerialId="
				+ playerSerialId + ", layer=" + layer + "]";
	}
}
