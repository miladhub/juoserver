package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

/**
 * Login confirm message. Sent as a response of a
 * {@link CharacterSelect} message.
 */
public class LoginConfirm extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x1B;
	
	private int charSerial;
	private short modelID;
	private short x;
	private short y;
	private byte z;
	private byte direction;
	private short notoriety;
	private short mapWidthMinusEight;
	private short mapHeightMinusEight;
	
	public LoginConfirm(int charSerial, short modelID, short x, short y, byte z,
			byte direction, short notoriety, short mapWidthMinusEight,
			short mapHeightMinusEight) {
		super(CODE, 37);
		this.charSerial = charSerial;
		this.modelID = modelID;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.notoriety = notoriety;
		this.mapWidthMinusEight = mapWidthMinusEight;
		this.mapHeightMinusEight = mapHeightMinusEight;
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putInt(charSerial);
		bb.putInt(0); // 4 unknown bytes
		bb.putShort(modelID);
		bb.putShort(x);
		bb.putShort(y);
		bb.put((byte) 0); // unknown byte
		bb.put(z);
		bb.put(direction);
		bb.putInt(0); // 4 unknown bytes
		bb.putInt(0); // 4 unknown bytes
		bb.put((byte) 0); // unknown byte
		bb.putShort(mapWidthMinusEight);
		bb.putShort(mapHeightMinusEight);
		bb.putShort((short) 0); // 2 unknown bytes
		bb.put((byte) 0); // unknown byte
		return bb;
	}

	public int getCharSerial() {
		return charSerial;
	}
	public short getModelID() {
		return modelID;
	}
	public short getX() {
		return x;
	}
	public short getY() {
		return y;
	}
	public byte getZ() {
		return z;
	}
	public byte getDirection() {
		return direction;
	}
	public short getNotoriety() {
		return notoriety;
	}
	public short getMapWidthMinusEight() {
		return mapWidthMinusEight;
	}
	public short getMapHeightMinusEight() {
		return mapHeightMinusEight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + charSerial;
		result = prime * result + direction;
		result = prime * result + mapHeightMinusEight;
		result = prime * result + mapWidthMinusEight;
		result = prime * result + modelID;
		result = prime * result + notoriety;
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
		LoginConfirm other = (LoginConfirm) obj;
		if (charSerial != other.charSerial)
			return false;
		if (direction != other.direction)
			return false;
		if (mapHeightMinusEight != other.mapHeightMinusEight)
			return false;
		if (mapWidthMinusEight != other.mapWidthMinusEight)
			return false;
		if (modelID != other.modelID)
			return false;
		if (notoriety != other.notoriety)
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
		return getName() + " [charSerial=" + charSerial + ", modelID="
				+ modelID + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", direction=" + direction + ", notoriety=" + notoriety
				+ ", mapWidthMinusEight=" + mapWidthMinusEight
				+ ", mapHeightMinusEight=" + mapHeightMinusEight + "]";
	}
}
