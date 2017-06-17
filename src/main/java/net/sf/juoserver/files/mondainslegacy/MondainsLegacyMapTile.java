package net.sf.juoserver.files.mondainslegacy;

import net.sf.juoserver.api.MapTile;

public final class MondainsLegacyMapTile implements MapTile {
	private int z;
	private int tileID;

	public MondainsLegacyMapTile(int z, int color) {
		super();
		this.z = z;
		this.tileID = color;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public int getTileID() {
		return tileID;
	}

	@Override
	public String toString() {
		return "MapTileImpl [z=" + z + ", tileID=" + tileID + "]";
	}
}
