package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.Mobile;

@Decodable(code = AttackRequest.CODE)
public class AttackRequest extends AbstractMessage {

	private static final long serialVersionUID = 1L;

	public static final int CODE = 0x05;
	private static final int LENGTH = 5;

	private int mobileID;
	
	public AttackRequest(byte[] contents) {
		this();		
		ByteBuffer bb = wrapContents(1, contents);
		mobileID = bb.getInt();
	}

	public AttackRequest(Mobile mobile) {
		this();
		this.mobileID = mobile.getSerialId();
	}

	public AttackRequest() {
		super(CODE, LENGTH);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public String toString() {
		return String.format("Attack to mobileId = %s", getMobileID());
	}
	
	public int getMobileID() { return mobileID; }
	public void setMobileID(int mobileID) { this.mobileID = mobileID;}
	

}
