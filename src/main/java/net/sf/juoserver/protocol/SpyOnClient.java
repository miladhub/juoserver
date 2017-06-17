package net.sf.juoserver.protocol;

import net.sf.juoserver.api.Decodable;

@Decodable(code=SpyOnClient.CODE)
public class SpyOnClient extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0xD9;
	public SpyOnClient(byte[] contents) {
		super(CODE, 268);
	}
	@Override
	public String toString() {
		return "SpyOnClient";
	}
	@Override
	public int hashCode() {
		return 0;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
}
