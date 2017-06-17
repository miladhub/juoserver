package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Notoriety;

@Decodable(code = MovementAck.CODE)
public class MovementAck extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x22;
	private int sequence;
	private Notoriety notoriety;
	public MovementAck(byte[] contents) {
		super(CODE, 3);
		ByteBuffer bb = wrapContents(contents);
		sequence = bb.get();
		notoriety = EnumUtils.byCode(bb.get(), Notoriety.class);
	}
	public MovementAck(int sequence, Notoriety notoriety) {
		super(CODE, 3);
		this.sequence = sequence;
		this.notoriety = notoriety;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put((byte) sequence);
		bb.put((byte) notoriety.getCode());
		return bb;
	}
	public int getSequence() {
		return sequence;
	}
	public Notoriety getNotoriety() {
		return notoriety;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((notoriety == null) ? 0 : notoriety.hashCode());
		result = prime * result + sequence;
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
		MovementAck other = (MovementAck) obj;
		if (notoriety != other.notoriety)
			return false;
		if (sequence != other.sequence)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "MovementAck [sequence=" + sequence + ", notoriety=" + notoriety
				+ "]";
	}
}
