package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Mobile;

public class DrawGamePlayer extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x20;
	private int createureId;
	private int bodyType;
	private int hue;
	private int x;
	private int y;
	private int z;
	private int direction;
	
	public DrawGamePlayer(int createureId, int bodyType,
			int hue, int x, int y, int z, int direction) {
		super(CODE, 19);
		this.createureId = createureId;
		this.bodyType = bodyType;
		this.hue = hue;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}

	public DrawGamePlayer(Mobile mobile) {
		this(mobile.getSerialId(), mobile.getModelId(), mobile.getHue(),
				mobile.getX(), mobile.getY(), mobile.getZ(),
				// TODO: use getDirectionWithRunningInfo() ?
				mobile.getDirection().getCode());
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putInt(createureId);
		bb.putShort((short) bodyType);
		bb.put((byte) 0);
		bb.putShort((short) hue);
		bb.put((byte) 0); // TODO: flags
		bb.putShort((short) x);
		bb.putShort((short) y);
		bb.putShort((short) 0);
		bb.put((byte) direction);
		bb.put((byte) z);
		return bb;
	}
	
	public int getCreateureId() {
		return createureId;
	}
	public int getBodyType() {
		return bodyType;
	}
	public int getHue() {
		return hue;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	public int getDirection() {
		return direction;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bodyType;
		result = prime * result + createureId;
		result = prime * result + direction;
		result = prime * result + hue;
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
		DrawGamePlayer other = (DrawGamePlayer) obj;
		if (bodyType != other.bodyType)
			return false;
		if (createureId != other.createureId)
			return false;
		if (direction != other.direction)
			return false;
		if (hue != other.hue)
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
		return "DrawGamePlayer [createureId=" + createureId + ", bodyType="
				+ bodyType + ", hue=" + hue + ", x=" + x + ", y=" + y + ", z="
				+ z + ", direction=" + direction + "]";
	}
}
