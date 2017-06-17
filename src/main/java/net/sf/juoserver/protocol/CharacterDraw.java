package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Map;

import net.sf.juoserver.api.CharacterStatus;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.Notoriety;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;

public class CharacterDraw extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x78;
	private int objectSerial;
	private int graphicId;
	private int x;
	private int y;
	private int z;
	private byte direction;
	private int hue;
	private CharacterStatus characterStatus;
	private Notoriety notoriety;
	private Map<Layer, Item> items;

	public CharacterDraw(Mobile mobile) {
		this(mobile.getSerialId(), mobile.getModelId(), mobile.getX(),
				mobile.getY(), mobile.getZ(), mobile.getDirectionWithRunningInfo(),
				mobile.getHue(), mobile.getCharacterStatus(), mobile.getNotoriety(),
				mobile.getItems());
	}
	
	private CharacterDraw(int objectSerial, int graphicId,
			int x, int y, int z, byte direction, int hue,
			CharacterStatus characterStatus, Notoriety notoriety,
			Map<Layer, Item> items) {
		super(CODE, getLength(objectSerial, graphicId, x, y, z,
				direction, hue, characterStatus, notoriety, items));
		this.objectSerial = objectSerial;
		this.graphicId = graphicId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.hue = hue;
		this.characterStatus = characterStatus;
		this.notoriety = notoriety;
		this.items = items;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.putInt(objectSerial);
		bb.putShort((short) graphicId);
		bb.putShort((short) x);
		bb.putShort((short) y);
		bb.put((byte) z);
		bb.put(direction);
		bb.putShort((short) hue);
		bb.put((byte) characterStatus.getCode());
		bb.put((byte) notoriety.getCode());
		// Loop over items on layers
		for (Layer layer : items.keySet()) {
			Item item = items.get(layer);
			
			int modelId = item.getModelId() & 0x7FFF;
			boolean writeHue = item.getHue() != 0;
			if (writeHue) {
				modelId |= 0x8000;
			}
			
			bb.putInt(item.getSerialId());
			bb.putShort((short) modelId);
			bb.put((byte) layer.getCode());
			
			if (writeHue) {
				bb.putShort((short) item.getHue());
			}
		}
		bb.put((byte) 0); // End byte
		return bb;
	}
	private static int getLength(int objectSerial, int graphicId,
			int x, int y, int z, byte direction, int hue,
			CharacterStatus characterStatus, Notoriety notoriety,
			Map<Layer, Item> items) {
		int l = 20; // Includes final byte
		for (Layer layer : items.keySet()) {
			l += 7;
			if (items.get(layer).getHue() != 0) {
				l += 2;
			}
		}
		return l;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((characterStatus == null) ? 0 : characterStatus.hashCode());
		result = prime * result + direction;
		result = prime * result + graphicId;
		result = prime * result + hue;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result
				+ ((notoriety == null) ? 0 : notoriety.hashCode());
		result = prime * result + objectSerial;
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
		CharacterDraw other = (CharacterDraw) obj;
		if (characterStatus != other.characterStatus)
			return false;
		if (direction != other.direction)
			return false;
		if (graphicId != other.graphicId)
			return false;
		if (hue != other.hue)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (notoriety != other.notoriety)
			return false;
		if (objectSerial != other.objectSerial)
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
		return "CharacterDraw [objectSerial=" + objectSerial + ", graphicId="
				+ graphicId + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", direction=" + direction + ", hue=" + hue
				+ ", characterStatus=" + characterStatus + ", notoriety="
				+ notoriety + ", items=" + items + "]";
	}
}
