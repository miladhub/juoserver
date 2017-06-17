package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.model.HasLightLevel;

public class PersonalLightLevel extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x4E;
	private int creatureId;
	private HasLightLevel lightLevel;
	public PersonalLightLevel(int creatureId, HasLightLevel lightLevel) {
		super(CODE, 6);
		this.creatureId = creatureId;
		this.lightLevel = lightLevel;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putInt(creatureId);
		bb.put(lightLevel.getLightLevel());
		return bb;
	}
	public HasLightLevel getLightLevel() {
		return lightLevel;
	}
	public int getCreatureId() {
		return creatureId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + creatureId;
		result = prime * result
				+ ((lightLevel == null) ? 0 : lightLevel.hashCode());
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
		PersonalLightLevel other = (PersonalLightLevel) obj;
		if (creatureId != other.creatureId)
			return false;
		if (lightLevel != other.lightLevel)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "PersonalLightLevel [creatureId=" + creatureId + ", lightLevel="
				+ lightLevel + "]";
	}
}
