package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

public class CharacterWarmode extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x72;
	private byte warMode;
	public CharacterWarmode(byte warMode) {
		super(CODE, 5);
		this.warMode = warMode;
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put(warMode);
		bb.put((byte) 0);
		bb.put((byte) 0x32);
		bb.put((byte) 0);
		return bb;
	}

	public byte getWarMode() {
		return warMode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + warMode;
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
		CharacterWarmode other = (CharacterWarmode) obj;
		if (warMode != other.warMode)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [warMode=" + warMode + "]";
	}
}
