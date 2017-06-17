package net.sf.juoserver.files.mondainslegacy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sf.juoserver.api.GenericFileEntryEncoder;
import net.sf.juoserver.api.MapFileReader;
import net.sf.juoserver.api.MapLocation;
import net.sf.juoserver.api.MapTile;
import net.sf.juoserver.files.FileReaderException;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

/**
 * Map file reader.
 * <p/>
 * This class provides <b>tile</b> information, given the X, Y coordinates of
 * the tile ({@link MapLocation}). The provided information are represented by a
 * {@link MondainsLegacyMapTile}.
 * <h1>Map file organization</h1>
 * <p/>
 * Each tile information is made of by a three-bytes sequence - hereafter
 * <i>cell</i> - within the selected map file (e.g., <tt>map0.mul</tt>). This
 * file, on the contrary than statics files or skills files, has no index file.
 * Instead it is accessed directly (this is possible because each entry is 3
 * bytes long).
 * <p/>
 * These three-bytes cells are organized within the file in so-called
 * <i>blocks</i>. Blocks are 8x8 cell matrixes (therefore made up by
 * <tt>8 * 8 * 3 = 192 bytes each</tt>), ordered in the map file from top to
 * bottom (Y) and then from left to right (X).
 * <p/>
 * The cells within a block are instead ordered from left to right (X) and then
 * from top to bottom (Y) (the contrary than blocks in the file).
 * <p/>
 * The common strategy used to access a single cell is to:
 * <ol>
 * <li>Calculate the <i>block number</i>: the sequential, 0-based, number that
 * identifies the block that the cell belongs to (this is just an ordinary
 * 0-based ordinal number, e.g., 0, 1, ...) - this is done by method
 * {@link #getBlockNumber(int, int)},</li>
 * <li>Calculate the <i>block offset</i>: the exact position within the map file
 * at which the block starts at,</li>
 * <li>Calculate the <i>cell offset</i>: the exact position <b>within the
 * block</b> at which the three bytes making up the tile cell start at.</li>
 * </ol>
 * A <b>block number</b> is calculated like this:
 * 
 * <pre>
 * int blockNumber = ((int) (x / 8)) * (mapHeight / 8) + ((int) (y / 8))
 * </pre>
 * 
 * where
 * <ul>
 * <li><tt>((int) (x / 8))</tt> is the X coordinate of the block of the
 * requested cell,</li>
 * <li><tt>((int) (y / 8))</tt> is the Y coordinate of the block of the
 * requested cell,</li>
 * <li><tt>(mapHeight / 8)</tt> is the number of blocks that the whole map
 * height contains.</li>
 * </ul>
 * The <b>block offset</b> is calculated like this:
 * 
 * <pre>
 * int blockOffset = blockNumber * 196 + 4
 * </pre>
 * 
 * where
 * <ul>
 * <li>4 is a two-bytes block header, whose content is unknown,</li>
 * <li>196 = 8 * 8 * 3 + 4 = block size.</li>
 * </ul>
 * The <b>cell offset</b> is calculated like this (this is the cell offset
 * within the block: you'll have to add this number to the <tt>blockOffset</tt>
 * ):
 * 
 * <pre>
 * int cellOffset = ( (y % 8) * 8 + (x % 8) ) * 3
 * </pre>
 * 
 * @see <a href="http://uo.stratics.com/heptazane/fileformats.shtml#3.8">File
 *      Formats</a>
 * @see #getBlockNumber(int, int)
 */
class MondainsLegacyMapFileReader implements MapFileReader {
	private Logger logger = Logger.getLogger(MondainsLegacyMapFileReader.class);
	private RandomAccessFile raf;
	private GenericFileEntryEncoder<MapTile> encoder;
	/**
	 * Map height. We need this information because of the way block cells are
	 * arranged in the map files.
	 * 
	 * @see #getEntryAt(MapLocation)
	 */
	private int mapHeight;

	public MondainsLegacyMapFileReader(File mapFile, int mapHeight) throws FileNotFoundException {
		super();
		this.raf = new RandomAccessFile(mapFile, "r");
		this.mapHeight = mapHeight;
		this.encoder = new GenericFileEntryEncoder<MapTile>() {
			@Override
			public MapTile encode(byte[] contents) {
				// Little endian again, just to make our life easier.. ;-)
				ByteBuffer bb = ByteBuffer.wrap(contents).order(
						ByteOrder.LITTLE_ENDIAN);
				int tileID = bb.getShort() & 0xFFFF; // Unsigned short (2 bytes)
				byte z = bb.get();
				return new MondainsLegacyMapTile(z, tileID);
			}
		};
	}

	/**
	 * Retrieves the tile information of the specified location.
	 * 
	 * @param location
	 *            2D-location
	 * @return the map tile at the specified location
	 */
	@Override
	public MapTile getEntryAt(MapLocation location) {
		if (location == null) {
			throw new IllegalArgumentException("location cannot be null");
		}
		int blockNumber = getBlockNumber(location.getX(), location.getY());
		// TODO: (LRU?-) cache locations (read & cache blocks instead?)
		try {
			// Seek the containing block
			// 196 = 8x8x3 + 4, 4 = file header bytes (unknown content)
			int blockOffset = blockNumber * 196 + 4;

			// Offset within the block (this time, X coordinate comes first)
			int cellX = location.getX() % 8;
			int cellY = location.getY() % 8;
			int cellNumber = cellY * 8 + cellX;
			raf.seek(blockOffset + cellNumber * 3);
		} catch (IOException e) {
			throw new FileReaderException(e);
		}

		// Read the cell
		byte[] buffer = new byte[3];
		try {
			raf.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			throw new FileReaderException(e);
		}
		logger.trace("Tile = " + Hex.encodeHexString(buffer).toUpperCase());
		return encoder.encode(buffer);
	}

	/**
	 * Returns the block number that the specified coordinates belong to.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @return block number
	 */
	private int getBlockNumber(int x, int y) {
		int blockX = x / 8; // X coordinate of the block
		int blockY = y / 8; // Y coordinate of the block
		return blockX * (mapHeight / 8) + blockY;
	}
}
