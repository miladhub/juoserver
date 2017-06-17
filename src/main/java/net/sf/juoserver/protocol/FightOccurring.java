package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Objects;

import net.sf.juoserver.api.Mobile;

public class FightOccurring extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x2F;
	
	private int attackerID;
	private int attackedID;
	
	public FightOccurring() {
		super(CODE, 10);
	}

	public FightOccurring(int attackerID, int attackedID) {
		this();
		this.attackerID = attackerID;
		this.attackedID = attackedID;
	}

	public FightOccurring(Mobile attacker, Mobile attacked) {
		this(attacker.getSerialId(), attacked.getSerialId());		
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = super.encode();
		buffer.put((byte) 0x00);
		buffer.putInt(attackerID);
		buffer.putInt(attackedID);
		return super.encode();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(attackerID, attackedID);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FightOccurring) {
			return Objects.equals(attackerID, ((FightOccurring) obj).attackerID) &&
					Objects.equals(attackedID, ((FightOccurring) obj).attackedID);
		}
		return false;
	}

	@Override
	public String toString() {
		return "FightOccurring [attackerID=" + attackerID + ", attackedID="
				+ attackedID + "]";
	}
}
