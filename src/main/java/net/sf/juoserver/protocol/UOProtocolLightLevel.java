package net.sf.juoserver.protocol;

import net.sf.juoserver.api.LightLevels;
import net.sf.juoserver.model.HasLightLevel;

public class UOProtocolLightLevel implements HasLightLevel {
	private int lightLevel;

	public UOProtocolLightLevel(LightLevels lightLevel) {
		super();
		switch (lightLevel) {
		case Black:
			this.lightLevel = 0x1F;
			break;
		case Day:
			this.lightLevel = 0;
			break;
		case OsiNight:
			this.lightLevel = 9;
			break;
		}
	}
	
	@Override
	public byte getLightLevel() {
		return (byte) (lightLevel & 0xFF);
	}
}
