package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;

@Decodable(code = PingPong.CODE)
public class PingPong extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x73;
	private byte sequenceNumber;
	private PingPong() { super(CODE, 2); }
	
	// ============ sent by the server ============
	public PingPong(byte sequenceNumber) {
		this();
		this.sequenceNumber = sequenceNumber;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put(sequenceNumber);
		return bb;
	}
	// ============ sent by the client ============
	public PingPong(byte[] contents) {
		this();
		sequenceNumber = contents[1];
	}
	public byte getSequenceNumber() {
		return sequenceNumber;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sequenceNumber;
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
		PingPong other = (PingPong) obj;
		if (sequenceNumber != other.sequenceNumber)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName() + " [sequenceNumber=" + sequenceNumber + "]";
	}
}
