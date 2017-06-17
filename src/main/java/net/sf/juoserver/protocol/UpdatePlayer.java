package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Mobile;

/**
 * Updates a player about a mobile's new position.
 */
public class UpdatePlayer extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x77;
	
	private int playerSerial;
	private int bodyType;
	private int x;
	private int y;
	private int z;
	private byte directionWithRunningInfo;
	private int hue;
	private int statusCode;
	private int notorietyCode;
	
	public UpdatePlayer(Mobile mobile) {
		super(CODE, 17);
		playerSerial = mobile.getSerialId();
		bodyType = mobile.getModelId();
		x = mobile.getX();
		y = mobile.getY();
		z = mobile.getZ();
		directionWithRunningInfo = mobile.getDirectionWithRunningInfo();
		hue = mobile.getHue();
		statusCode = mobile.getCharacterStatus().getCode();
		notorietyCode = mobile.getNotoriety().getCode();
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putInt(playerSerial);
		bb.putShort((short) bodyType);
		bb.putShort((short) x);
		bb.putShort((short) y);
		bb.put((byte) z);
		bb.put(directionWithRunningInfo);
		bb.putShort((short) hue);
		bb.put((byte) statusCode);
		bb.put((byte) notorietyCode);
		return bb;
	}

	public int getPlayerSerial() {
		return playerSerial;
	}

	public int getBodyType() {
		return bodyType;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public byte getDirectionWithRunningInfo() {
		return directionWithRunningInfo;
	}

	public int getHue() {
		return hue;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public int getNotorietyCode() {
		return notorietyCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bodyType;
		result = prime * result + directionWithRunningInfo;
		result = prime * result + hue;
		result = prime * result + notorietyCode;
		result = prime * result + playerSerial;
		result = prime * result + statusCode;
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
		UpdatePlayer other = (UpdatePlayer) obj;
		if (bodyType != other.bodyType)
			return false;
		if (directionWithRunningInfo != other.directionWithRunningInfo)
			return false;
		if (hue != other.hue)
			return false;
		if (notorietyCode != other.notorietyCode)
			return false;
		if (playerSerial != other.playerSerial)
			return false;
		if (statusCode != other.statusCode)
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
		return "UpdatePlayer [playerSerial=" + playerSerial + ", bodyType="
				+ bodyType + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", directionWithRunningInfo=" + directionWithRunningInfo
				+ ", hue=" + hue + ", statusCode=" + statusCode
				+ ", notorietyCode=" + notorietyCode + "]";
	}
}
