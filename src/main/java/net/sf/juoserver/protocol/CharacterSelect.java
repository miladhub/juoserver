package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;

@Decodable(code=CharacterSelect.CODE)
public class CharacterSelect extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x5D;
	private String charName;
	private int charId;
	private CharacterSelect() {
		super(CODE, 73);
	}
	
	public CharacterSelect(byte[] contents) {
		this();
		ByteBuffer bb = wrapContents(contents);
		bb.getInt(); // 0xEDEDEDED
		charName = MessagesUtils.getString(contents, 5, 34);
		bb.position(bb.position() + 30);
		
		for (int i = 0; i < 33; i++) { bb.get(); } // Mostly zeroes
		charId = bb.get();
		bb.getInt(); // Client IP (don't trust)
	}
	
	public CharacterSelect(String charName, int charId) {
		this();
		this.charName = charName;
		this.charId = charId;
	}

	public String getCharName() {
		return charName;
	}
	public int getCharId() {
		return charId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + charId;
		result = prime * result
				+ ((charName == null) ? 0 : charName.hashCode());
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
		CharacterSelect other = (CharacterSelect) obj;
		if (charId != other.charId)
			return false;
		if (charName == null) {
			if (other.charName != null)
				return false;
		} else if (!charName.equals(other.charName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return getName() + " [charId=" + charId + ", charName=" + charName
				+ "]";
	}
}
