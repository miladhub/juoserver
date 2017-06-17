package net.sf.juoserver.model;

import net.sf.juoserver.api.GameStatus;
import net.sf.juoserver.api.LightLevels;
import net.sf.juoserver.api.Season;

public class UOGameStatus implements GameStatus {
	private final LightLevels lightLevel;
	private final Season season;

	public UOGameStatus(LightLevels lightLevel, Season season) {
		this.lightLevel = lightLevel;
		this.season = season;
	}

	@Override
	public LightLevels getLightLevel() {
		return lightLevel;
	}

	@Override
	public Season getSeason() {
		return season;
	}
}
