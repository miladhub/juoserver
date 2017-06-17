package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.model.HasLightLevel;

public class OverallLightLevel extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x4F;
	private HasLightLevel lightLevel;
	public OverallLightLevel(HasLightLevel lightLevel) {
		super(CODE, 2);
		this.lightLevel = lightLevel;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put(lightLevel.getLightLevel());
		return bb;
	}
	public HasLightLevel getLightLevel() {
		return lightLevel;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		OverallLightLevel other = (OverallLightLevel) obj;
		if (lightLevel != other.lightLevel)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "OverallLightLevel [lightLevel=" + lightLevel + "]";
	}
}
