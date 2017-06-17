package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.juoserver.api.Container;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Point2D;

public class ContainerItems extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x3C;
	private int containerSerialId;
	private List<? extends Item> items;
	private Map<Item, Point2D> itemsPositions;

	public ContainerItems(Container cont) {
		super(CODE, getLength(cont));
		containerSerialId = cont.getSerialId();
		items = new ArrayList<Item>(cont.getItems());
		itemsPositions = cont.getItemsPositions();
	}

	private static int getLength(Container cont) {
		return 5 + cont.getItems().size() * (4 + 2 + 1 + 2 + 2 + 2 + 4 + 2);
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.putShort((short) items.size());
		for (Item item : items) {
			bb.putInt(item.getSerialId());
			bb.putShort((short) item.getModelId());
			bb.put((byte) 0);
			bb.putShort((short) 1); //TODO: amount
			bb.putShort((short) itemsPositions.get(item).getX());
			bb.putShort((short) itemsPositions.get(item).getY());
			
			bb.putInt(containerSerialId);
			bb.putShort((short) item.getHue());
		}
		return bb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + containerSerialId;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result
				+ ((itemsPositions == null) ? 0 : itemsPositions.hashCode());
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
		ContainerItems other = (ContainerItems) obj;
		if (containerSerialId != other.containerSerialId)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (itemsPositions == null) {
			if (other.itemsPositions != null)
				return false;
		} else if (!itemsPositions.equals(other.itemsPositions))
			return false;
		return true;
	}
}
