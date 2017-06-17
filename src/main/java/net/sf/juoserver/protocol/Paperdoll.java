package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

public class Paperdoll extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x88;
	private int serialId;
	private String txt;
	private boolean canChangePaperdoll;
	private boolean warMode;
	public Paperdoll(int serialId, String txt, boolean canChangePaperdoll, boolean warMode) {
		super(CODE, 66);
		this.serialId = serialId;
		this.txt = txt;
		this.canChangePaperdoll = canChangePaperdoll;
		this.warMode = warMode;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		byte flags = 0x00;
		if (warMode) {
			flags |= 0x01;
		}
		if (canChangePaperdoll) {
			flags |= 0x02;
		}
		bb.putInt(serialId);
		bb.put(MessagesUtils.padString(txt, 60));
		bb.put(flags);
		return bb;
	}

	public int getSerialId() {
		return serialId;
	}
	public String getTxt() {
		return txt;
	}
	public boolean isCanChangePaperdoll() {
		return canChangePaperdoll;
	}
	public boolean isWarMode() {
		return warMode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (canChangePaperdoll ? 1231 : 1237);
		result = prime * result + serialId;
		result = prime * result + ((txt == null) ? 0 : txt.hashCode());
		result = prime * result + (warMode ? 1231 : 1237);
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
		Paperdoll other = (Paperdoll) obj;
		if (canChangePaperdoll != other.canChangePaperdoll)
			return false;
		if (serialId != other.serialId)
			return false;
		if (txt == null) {
			if (other.txt != null)
				return false;
		} else if (!txt.equals(other.txt))
			return false;
		if (warMode != other.warMode)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Paperdoll [serialId=" + serialId + ", txt=" + txt
				+ ", canChangePaperdoll=" + canChangePaperdoll + ", warMode="
				+ warMode + "]";
	}
}
