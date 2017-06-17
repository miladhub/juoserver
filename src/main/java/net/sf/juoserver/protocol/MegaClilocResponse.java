package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.api.JUoEntity;
import net.sf.juoserver.model.Clilocs;

// TODO: by now we simply want to be able to display an approaching mobile's name above their head.
// In time, we should be able to list all of their properties and do the same for items in order for tooltips to work.
public abstract class MegaClilocResponse<T extends JUoEntity> extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0xD6;
	private T obj;
	
	private MegaClilocResponse(T obj, int length) {
		super(CODE, length);
		this.obj = obj;
	}
	
	private static final class MobileMegaClilocResponse extends MegaClilocResponse<Mobile> {
		private static final long serialVersionUID = 1L;

		public MobileMegaClilocResponse(Mobile mobile, int length) {
			super(mobile, length);
		}
		
		@Override
		public void encodeObject(Mobile mobile, ByteBuffer bb) {
			bb.putInt(Clilocs.PREFIX_NAME_SUFFIX.getCode()); // Cliloc ID
			bb.putShort(getUnicodeStringLength(mobile)); // Length, unicode string
			MessagesUtils.putReverseUnicodeString(bb, mobile.getPrefixNameSuffix());
		}

		@Override
		public int revisionHashCode(Mobile obj) {
			return RevisionUtils.mobileRevisionHashCode(obj);
		}
	}
	
	private static final class ItemMegaClilocResponse extends MegaClilocResponse<Item> {
		private static final long serialVersionUID = 1L;

		public ItemMegaClilocResponse(Item item, int length) {
			super(item, length);
		}

		@Override
		public void encodeObject(Item item, ByteBuffer bb) {
			bb.putInt(RevisionUtils.getItemClilocID(item));
			bb.put((byte) 0);
			bb.put((byte) 0);
		}

		@Override
		public int revisionHashCode(Item obj) {
			return RevisionUtils.itemRevisionHashCode(obj);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((obj == null) ? 0 : obj.hashCode());
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
		MegaClilocResponse<?> other = (MegaClilocResponse<?>) obj;
		if (this.obj == null) {
			if (other.obj != null)
				return false;
		} else if (!this.obj.equals(other.obj))
			return false;
		return true;
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.putShort((short) 1);
		
		bb.putInt(obj.getSerialId());
		bb.putShort((short) 0);
		bb.putInt(revisionHashCode(obj));

		encodeObject(obj, bb);
		
		// TODO: add more properties (for both cases)
		
		bb.putInt(0); // 4 final bytes
		return bb;
	}

	public abstract int revisionHashCode(T obj);

	public abstract void encodeObject(T obj, ByteBuffer bb);

	public static AbstractMessage createMobileMegaClilocResponse(Mobile mobile) {
		return new MobileMegaClilocResponse(mobile, mobileLength(mobile));
	}

	public static AbstractMessage createItemMegaClilocResponse(Item item) {
		return new ItemMegaClilocResponse(item, itemLength(item));
	}
	
	private static int mobileLength(Mobile mobile) {
		return 1 + 2 + 2 + 4 + 2 + 4  + 4 + 2 + getUnicodeStringLength(mobile) + 4;
	}
	
	private static short getUnicodeStringLength(Mobile mobile) {
		return (short) (2 * mobile.getPrefixNameSuffix().length());
	}
	
	private static int itemLength(Item item) {
		return 1 + 2 + 2 + 4 + 2 + 4 + 4 + 2 + 4;
	}
}
