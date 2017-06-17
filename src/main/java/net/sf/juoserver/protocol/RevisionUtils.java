package net.sf.juoserver.protocol;

import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;
import net.sf.juoserver.model.Clilocs;

public class RevisionUtils {
	static int getItemClilocID(Item it) {
		if (it.getModelId() < 0x4000) {
			return 1020000 + it.getModelId();
		} else {
			return 1078872 + it.getModelId();
		}
	}

	static int mobileRevisionHashCode(Mobile mobile) {
		int hash = addHashTo(0, Clilocs.PREFIX_NAME_SUFFIX.getCode());
		return addHashTo(hash, mobile.getPrefixNameSuffix().hashCode());
	}

	static int itemRevisionHashCode(Item item) {
		int hash = addHashTo(0, Clilocs.ITEM_NAME.getCode());
		return addHashTo(hash, getItemClilocID(item));
	}
	
	private static int addHashTo(int originalHash, int value) {
		originalHash ^= value & 0x3FFFFFF;
		originalHash ^= (value >> 26) & 0x3F;
		return originalHash;
	}
}
