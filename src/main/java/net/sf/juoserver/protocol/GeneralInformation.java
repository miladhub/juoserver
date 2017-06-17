package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import net.sf.juoserver.api.Coded;
import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.model.ClientFlag;

/**
 * General information packet.
 * @see <a href="http://docs.polserver.com/packets/index.php?Packet=0xBF">docs</a>
 */
@Decodable(code = GeneralInformation.CODE)
public class GeneralInformation extends AbstractMessage {
	// NOTE: we cannot create a subclass for each sub-command
	// type, because there must be a 1-1 relationship between
	// the @Decodable code and the message class representing
	// it. If we did create such classes, each of them would
	// be annotated with the same code, thus breaking the
	// MessageReader's ability to pick up the right message type
	// basing on the code.
	private static final long serialVersionUID = 1L;
	protected final static int CODE = 0xBF;
	public enum SubcommandType implements Coded {
		ScreenSize(5), ClientLanguage(0x0B), ClientType(0x0F),
		Unhandled(0x24),
		SetCursorHueSetMap(8), EnableMapDiff(0x12),
		CloseStatusGump(0xc);
		private int code;
		private SubcommandType(int subcmd) {
			this.code = subcmd;
		}
		public static SubcommandType valueOf(int subcmd) {
			for (SubcommandType sc : values()) {
				if (sc.getCode() == subcmd) {
					return sc;
				}
			}
			throw new IllegalArgumentException("Bad subcmd " + subcmd);
		}
		@Override
		public int getCode() {
			return code;
		}
	}
	public static class ScreenSize extends Subcommand<GeneralInformation, SubcommandType> {
		private static final long serialVersionUID = 1L;
		private short x, y;
		public ScreenSize(short x, short y) {
			super(SubcommandType.ScreenSize);
			this.x = x;
			this.y = y;
		}
		public short getX() {
			return x;
		}
		public short getY() {
			return y;
		}
		@Override
		public String toString() {
			return "ScreenSize [x=" + x + ", y=" + y + "]";
		}
	}
	public static class ClientLanguage extends Subcommand<GeneralInformation, SubcommandType> {
		private static final long serialVersionUID = 1L;
		private String language;
		public ClientLanguage(String language) {
			super(SubcommandType.ClientLanguage);
			this.language = language;
		}
		public String getLanguage() {
			return language;
		}
		@Override
		public String toString() {
			return "ClientLanguage [language=" + language + "]";
		}
	}
	public static class ClientType extends Subcommand<GeneralInformation, SubcommandType> {
		private static final long serialVersionUID = 1L;
		private Set<ClientFlag> clientFlags;
		public ClientType(int clientType) {
			super(SubcommandType.ClientType);
			this.clientFlags = ClientFlag.parse( clientType );
		}
		public Set<ClientFlag> getClientType() {
			return clientFlags;
		}
		@Override
		public String toString() {
			return "ClientType [clientFlags=" + clientFlags + "]";
		}
	}
	public static class SetCursorHueSetMap extends Subcommand<GeneralInformation, SubcommandType> {
		private static final long serialVersionUID = 1L;
		/**
		 * Maps:
		 * <ul>
		 * <li>0 = Felucca, unhued / BRITANNIA map,</li>
		 * <li>1 = Trammel, hued gold / BRITANNIA map,</li>
		 * <li>2 = Ilshenar map,</li>
		 * <li>3 = Malas,</li>
		 * <li>4 = Tokuno,</li>
		 * <li>5 = TerMur.</li>
		 * </ul>
		 * The 0-based number is the map ID.
		 * In <tt>MapDefinitions</tt> (<tt>RegisterMap(...)</tt>, RunUO), it corresponds to
		 * <tt>mapID</tt> (which in turn is the same thing as the <tt>mapIndex</tt>, I guess).
		 * <p/>
		 * The <tt>fileIndex</tt> number points to the actual map files:
		 * <ul>
		 * <li><tt>map{0}.mul</tt> - terrain and altitude, Z, information with no statics,</li>
		 * <li><tt>staidx{0}.mul</tt> - index file for the next one,</li>
		 * <li><tt>statics{0}.mul</tt> - statics, they act as an "items layer" above the terrain: walls, etc.</li>
		 * </ul>
		 * See class <tt>TileMatrix</tt> (RunUO).
		 * <p/>
		 * Map patches (if we want to support them at all):
		 * <ul>
		 * <li><tt>mapID</tt> - indexes the patch files: <tt>mapdif{0}.mul</tt>, <tt>mapdifl{0}.mul</tt>,
		 * <tt>stadif{0}.mul</tt>, <tt>stadifl{0}.mul</tt>, <tt>stadifi{0}.mul</tt></li>
		 * </ul>
		 * See class <tt>TileMatrixPatch</tt> (RunUO).
		 */
		private byte map;
		public SetCursorHueSetMap(byte map) {
			super(SubcommandType.SetCursorHueSetMap);
			this.map = map;
		}
		public byte getMap() {
			return map;
		}
		@Override
		public String toString() {
			return "SetCursorHueSetMap [map=" + map + "]";
		}
	}
	public static class EnableMapDiff extends Subcommand<GeneralInformation, SubcommandType> {
		private static final long serialVersionUID = 1L;
		private int numberOfMaps;
		private int[] numberOfMapPatches;
		private int[] numberOfStaticPatches;
		public EnableMapDiff(int numberOfMaps,
				int[] numberOfMapPatches, int[] numberOfStaticPatches) {
			super(SubcommandType.EnableMapDiff);
			this.numberOfMaps = numberOfMaps;
			if (numberOfMaps != numberOfMapPatches.length ||
					numberOfMaps != numberOfStaticPatches.length) {
				throw new IllegalArgumentException("Bad size");
			}
			this.numberOfMapPatches = numberOfMapPatches;
			this.numberOfStaticPatches = numberOfStaticPatches;
		}
		public int getNumberOfMaps() {
			return numberOfMaps;
		}
		public int[] getNumberOfMapPatches() {
			return numberOfMapPatches;
		}
		public int[] getNumberOfStaticPatches() {
			return numberOfStaticPatches;
		}
		@Override
		public String toString() {
			return "EnableMapDiff [numberOfMaps=" + numberOfMaps
					+ ", numberOfMapPatches="
					+ Arrays.toString(numberOfMapPatches)
					+ ", numberOfStaticPatches="
					+ Arrays.toString(numberOfStaticPatches) + "]";
		}
	}
	private static class CloseStatusGump extends Subcommand<GeneralInformation, SubcommandType> {

		private int mobileId;
		
		public CloseStatusGump(int mobileId) {
			super(SubcommandType.CloseStatusGump);
			this.mobileId = mobileId;
		}
		
		public int getMobileId() {
			return mobileId;
		}

		@Override
		public String toString() {
			return "CloseStatusGump [mobileId=" + mobileId + "]";
		}
		
	}
	private Subcommand<GeneralInformation, SubcommandType> subCommand;
	// ============== client ==============
	public GeneralInformation(byte[] contents) {
		super(CODE, MessagesUtils.getLengthFromSecondAndThirdByte(contents));
		ByteBuffer bb = wrapContents(3, contents);
		SubcommandType subcommandType = SubcommandType.valueOf(bb.getShort());
		switch (subcommandType) {
		case ScreenSize:
			bb.getShort(); // 0x0000, unknown
			subCommand = new ScreenSize(bb.getShort(), bb.getShort());
			break;
		case ClientLanguage:
			subCommand = new ClientLanguage(MessagesUtils.getString(contents, 5, 8));
			break;
		case ClientType:
			bb.get(); // 0x0A, unknown
			subCommand = new ClientType(bb.getInt());
			break;
		case CloseStatusGump:
			subCommand = new CloseStatusGump(bb.getInt());
		case Unhandled:
			break;
		default:
			throw new IllegalStateException("Unknown subcommand: " + subcommandType);
		}
	}
	// ============== client ==============
	// ============== server ==============
	public GeneralInformation(Subcommand<GeneralInformation, SubcommandType> subCommand) {
		super(CODE, getLength(subCommand));
		this.subCommand = subCommand;
	}
	private static int getLength(Subcommand<GeneralInformation, SubcommandType> subCommand) {
		SubcommandType subcommandType = SubcommandType.valueOf(subCommand
				.getType().getCode());
		switch (subcommandType) {
		case SetCursorHueSetMap:
			return 6;
		case EnableMapDiff:
			return 41;
		}
		throw new IllegalStateException("Unknown subcommand: " + subcommandType);
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		SubcommandType subcommandType = SubcommandType.valueOf(subCommand
				.getType().getCode());
		bb.putShort((short) subcommandType.getCode());
		switch (subcommandType) {
		case SetCursorHueSetMap:
			bb.put(((SetCursorHueSetMap) subCommand).getMap());
			break;
		case EnableMapDiff:
			EnableMapDiff enableMapDiff = (EnableMapDiff) subCommand;
			int numberOfMaps = enableMapDiff.getNumberOfMaps();
			bb.putInt(numberOfMaps);
			for (int i = 0; i < numberOfMaps; i++) {
				bb.putInt(enableMapDiff.getNumberOfMapPatches()[i]);
				bb.putInt(enableMapDiff.getNumberOfStaticPatches()[i]);
			}
			break;
		default:
			throw new IllegalStateException("Unknown subcommand: " + subcommandType);
		}
		return bb;
	}
	// ============== server ==============
	public Subcommand<GeneralInformation, SubcommandType> getSubCommand() {
		return subCommand;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((subCommand == null) ? 0 : subCommand.hashCode());
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
		GeneralInformation other = (GeneralInformation) obj;
		if (subCommand == null) {
			if (other.subCommand != null)
				return false;
		} else if (!subCommand.equals(other.subCommand))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [subCommand=" + subCommand + "]";
	}
}
