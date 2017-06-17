package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;

@Decodable(code = GetPlayerStatus.CODE)
public class GetPlayerStatus extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x34;
	public static enum PlayerStatusRequest {
		GodClient(0),
		Stats(4),
		Skills(5);
		private int type;
		private PlayerStatusRequest(int type) {
			this.type = type;
		}
		public static PlayerStatusRequest valueOfByType(int type) {
			for (PlayerStatusRequest value : values()) {
				if (type == value.getType()) {
					return value;
				}
			}
			return null;
		}
		public int getType() {
			return type;
		}
	}
	private byte type;
	private int serial;
	public GetPlayerStatus(byte[] contents) {
		super(CODE, 10);
		ByteBuffer bb = wrapContents(contents);
		bb.getInt(); // 0xEDEDEDED
		type = bb.get();
		serial = bb.getInt();
	}
	public PlayerStatusRequest getRequest() {
		return PlayerStatusRequest.valueOfByType( getType() );
	}
	public byte getType() {
		return type;
	}
	public int getSerial() {
		return serial;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + serial;
		result = prime * result + type;
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
		GetPlayerStatus other = (GetPlayerStatus) obj;
		if (serial != other.serial)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [type=" + type + ", serial=" + serial + "]";
	}
}
