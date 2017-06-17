package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import net.sf.juoserver.model.City;
import net.sf.juoserver.model.Flag;
import net.sf.juoserver.model.PlayingCharacter;

public class CharacterList extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	public static final int CODE = 0xA9;
	private List<PlayingCharacter> pcs;
	private List<City> cities;
	private Flag[] flags;
	public CharacterList(List<PlayingCharacter> pcs, List<City> cities, Flag... flags) {
		super(CODE, getLength(pcs, cities));
		this.pcs = pcs;
		this.cities = cities;
		this.flags = flags;
	}
	private static int getLength(List<PlayingCharacter> pcs, List<City> cities) {
		return 1 + 2 + 1 + pcs.size() * (30 + 30) + 1 + cities.size() * (1 + 31 + 31) + 4;
	}
	
	public final List<PlayingCharacter> getPcs() {
		return pcs;
	}
	public final List<City> getCities() {
		return cities;
	}
	public final Flag[] getFlags() {
		return flags;
	}
	
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.put((byte) pcs.size());
		for (PlayingCharacter pc : pcs) {
			bb.put( MessagesUtils.padString(pc.getUser(), 30) );
			bb.put( MessagesUtils.padString(pc.getPassword(), 30) );
		}
		bb.put((byte) cities.size());
		int i = 0;
		for (City city : cities) {
			bb.put((byte) i++);
			bb.put( MessagesUtils.padString(city.getName(), 31) );
			bb.put( MessagesUtils.padString(city.getLocationName(), 31) );
		}
		int flagsValue = 0;
		for (Flag f : flags) {
			flagsValue |= f.getValue();
		}
		bb.putInt(flagsValue);
		return bb;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cities == null) ? 0 : cities.hashCode());
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + ((pcs == null) ? 0 : pcs.hashCode());
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
		CharacterList other = (CharacterList) obj;
		if (cities == null) {
			if (other.cities != null)
				return false;
		} else if (!cities.equals(other.cities))
			return false;
		if (!Arrays.equals(flags, other.flags))
			return false;
		if (pcs == null) {
			if (other.pcs != null)
				return false;
		} else if (!pcs.equals(other.pcs))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [chars=" + pcs + ", cities=" + cities
				+ ", flags=" + Arrays.toString(flags) + "]";
	}
}
