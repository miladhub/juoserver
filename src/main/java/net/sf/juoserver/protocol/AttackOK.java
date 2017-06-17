package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Objects;

import net.sf.juoserver.api.Mobile;

public class AttackOK extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x30;
	
	private int mobileID;
	
	public AttackOK() {
		super(CODE, 5);		
	}

	public AttackOK(int mobileID) {
		this();
		this.mobileID = mobileID;
	}

	public AttackOK(Mobile mobile) {
		this(mobile.getSerialId());		
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = super.encode();
		buffer.putInt(mobileID);
		return buffer;
	}
	
	public int getMobileID() {
		return mobileID;
	}
	
	@Override
	public int hashCode() {		
		return Objects.hash(mobileID);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AttackOK) {
			return Objects.equals(mobileID, ((AttackOK) obj).mobileID);
		}
		return false;
	}

	@Override
	public String toString() {
		return "AttackOK [mobileID=" + mobileID + "]";
	}
}
