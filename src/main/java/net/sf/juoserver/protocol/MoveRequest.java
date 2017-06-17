package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Direction;

@Decodable(code=MoveRequest.CODE)
public class MoveRequest extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x02;
	private Direction direction;
	private int sequence;
	private int fastwalkPrevKey;
	private boolean running;
	
	private MoveRequest() {
		super(CODE, 7);
	}
	
	public MoveRequest(byte[] contents) {
		this();
		ByteBuffer bb = wrapContents(contents);
		byte directionWithRunningInfo = bb.get();
		// When running, it is Direction | 0x80, therefore direction becomes
		// 0x80, 0x81, and so on.
		direction = EnumUtils.byCode(directionWithRunningInfo & 0x07, Direction.class);
		running = (directionWithRunningInfo & 0xF0) == 0x80;
		sequence = bb.get() & 0xFF;
		fastwalkPrevKey = bb.getInt();
	}
	
	public MoveRequest(Direction direction, int sequence, int fastwalkPrevKey,
			boolean running) {
		this();
		this.direction = direction;
		this.sequence = sequence;
		this.fastwalkPrevKey = fastwalkPrevKey;
		this.running = running;
	}
	
	public Direction getDirection() {
		return direction;
	}
	public boolean isRunning() {
		return running;
	}
	public int getSequence() {
		return sequence;
	}
	public int getFastwalkPrevKey() {
		return fastwalkPrevKey;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + fastwalkPrevKey;
		result = prime * result + (running ? 1231 : 1237);
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
		MoveRequest other = (MoveRequest) obj;
		if (direction != other.direction)
			return false;
		if (fastwalkPrevKey != other.fastwalkPrevKey)
			return false;
		if (running != other.running)
			return false;
		if (sequence != other.sequence)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MoveRequest [direction=" + direction + ", sequence=" + sequence
				+ ", fastwalkPrevKey=" + fastwalkPrevKey + "]";
	}
}
