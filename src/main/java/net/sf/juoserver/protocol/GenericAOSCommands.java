package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Objects;

import net.sf.juoserver.api.Coded;
import net.sf.juoserver.api.Decodable;

@Decodable(code=GenericAOSCommands.CODE)
public class GenericAOSCommands extends AbstractMessage {
	
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0xd7;

	public static enum SubCommandType implements Coded {
		GUILD_BUTTON(0x28),
		QUEST_BUTTON(0x32);
		
		private int code;

		private SubCommandType(int code) {
			this.code = code;
		}
				
		public int getCode() {
			return code;
		}
		
		public static SubCommandType valueOf(int code) {
			for (SubCommandType commandType : values()) {
				if (commandType.getCode() == code) {
					return commandType;							
				}
			}
			throw new IllegalArgumentException("Bad subcmd " + code);
		}
	}
	
	public static class GuildButton extends Subcommand<GenericAOSCommands, SubCommandType> {		

		private static final long serialVersionUID = 1L;
		
		public GuildButton() {
			super(SubCommandType.GUILD_BUTTON);
		}
	}
	
	public static class QuestButton extends Subcommand<GenericAOSCommands, SubCommandType> {
		
		private static final long serialVersionUID = 1L;

		public QuestButton() {
			super(SubCommandType.QUEST_BUTTON);
		}
		
	}
	
	//TODO SubCommand 0x02: Backup
	//TODO SubCommand 0x03: Restore
	//TODO SubCommand 0x04: Commit
	//TODO SubCommand 0x05: DeleteItem
	//TODO SubCommand 0x06: AddItem
	//TODO SubCommand 0x0C: Exit House Tool
	//TODO SubCommand 0x0D: Change Stairs
	//TODO SubCommand 0x0E: Synch Button
	//TODO SubCommand 0x10: Clear Button
	//TODO SubCommand 0x12: ChangeFloor
	//TODO SubCommand 0x1A: Revert Button
	//TODO SubCommand 0x19: Combat Book Abilities
	
	private int mobileID;
	private Subcommand<GenericAOSCommands, SubCommandType> subcommand;
	
	public GenericAOSCommands(byte[] contents) {
		super(CODE, MessagesUtils.getLengthFromSecondAndThirdByte(contents));
		ByteBuffer buffer = wrapContents(contents);
		buffer.getShort(); // ignore length
		this.mobileID = buffer.getInt();
		SubCommandType subCommandType = SubCommandType.valueOf(buffer.getShort()); 
		switch (subCommandType) {
		case GUILD_BUTTON:
			this.subcommand = new GuildButton();// Ignore last byte
			break;
		case QUEST_BUTTON:
			this.subcommand = new QuestButton();// Ignore last byte	
			break;
		default:
			throw new IllegalStateException("Unknown subcommand: " + subCommandType);
		}
	}	
	
	public int getMobileID() {
		return mobileID;
	}
	
	public Subcommand<GenericAOSCommands, SubCommandType> getSubcommand() {
		return subcommand;
	}
	
	@Override
	public int hashCode() {		
		return Objects.hash(mobileID, subcommand);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GenericAOSCommands) {
			return Objects.equals(mobileID, ((GenericAOSCommands) obj).mobileID) &&
					Objects.equals(subcommand, ((GenericAOSCommands) obj).subcommand);
		}
		return false;
	}

}
