package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.CharacterStatus;
import net.sf.juoserver.api.Decodable;

@Decodable(code=WarMode.CODE)
public class WarMode extends AbstractMessage {
	 
	private static final long serialVersionUID = 1L;

	protected static final int CODE = 0x72;

	private byte mode;

	public WarMode() {
		super(CODE, 5);
	}
	
	public WarMode(byte[] data) {
		this();
		mode = data[1];
	}

	public WarMode(CharacterStatus characterStatus) {
		this();
		mode = (byte)(characterStatus.equals(CharacterStatus.WarMode)? 1 : 0);
	}
	
	public boolean isWar() {
		return mode==1;
	}
	
	@Override
	public ByteBuffer encode() {		
		ByteBuffer buffer = super.encode();
		buffer.put(mode);
		buffer.put((byte)00);
		buffer.put((byte)32);
		buffer.put((byte)00);
		return buffer;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mode;
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
		WarMode other = (WarMode) obj;
		if (mode != other.mode)
			return false;
		return true;
	}	
	
}
