package net.sf.juoserver.api;

/**
 * Map tile information:
 * <ul>
 * <li>Z (altitude),</li>
 * <li>Tile ID (which can be looked up within <i>Inside UO / Artwork / Landscape
 * Tiles</i>).</li>
 * </ul>
 */
public interface MapTile {
	int getZ();

	int getTileID();
}
