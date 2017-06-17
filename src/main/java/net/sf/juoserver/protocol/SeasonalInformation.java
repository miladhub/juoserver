package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Season;

public class SeasonalInformation extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0xBC;
	private Season season;
	private boolean playSounds;
	public SeasonalInformation(Season season, boolean playSounds) {
		super(CODE, 3);
		this.season = season;
		this.playSounds = playSounds;
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.put((byte) season.ordinal());
		bb.put((byte) (playSounds? 1 : 0));
		return bb;
	}
	public Season getSeason() {
		return season;
	}
	public boolean isPlaySounds() {
		return playSounds;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (playSounds ? 1231 : 1237);
		result = prime * result + ((season == null) ? 0 : season.hashCode());
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
		SeasonalInformation other = (SeasonalInformation) obj;
		if (playSounds != other.playSounds)
			return false;
		if (season != other.season)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SeasonalInformation [season=" + season + ", playSounds="
				+ playSounds + "]";
	}
}
