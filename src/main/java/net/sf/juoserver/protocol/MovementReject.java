package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

public class MovementReject extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x21;
	private int sequence, x, y, z;
	private byte direction;
	public MovementReject(int sequence, int x, int y,
			int z, byte direction) {
		super(CODE, 8);
		this.sequence = sequence;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put((byte) sequence);
		bb.putShort((short) x);
		bb.putShort((short) y);
		bb.put(direction);
		bb.put((byte) z);
		return bb;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + direction;
		result = prime * result + sequence;
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
		MovementReject other = (MovementReject) obj;
		if (direction != other.direction)
			return false;
		if (sequence != other.sequence)
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
		return "MovementReject [sequence=" + sequence + ", x=" + x + ", y=" + y
				+ ", z=" + z + ", direction=" + direction + "]";
	}
}
