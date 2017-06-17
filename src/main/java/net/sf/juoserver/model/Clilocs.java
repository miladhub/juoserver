package net.sf.juoserver.model;

import net.sf.juoserver.api.Coded;

public enum Clilocs implements Coded {
	
	/**
	 * ~1_PREFIX~ ~2_NAME~ ~3_SUFFIX~
	 */
	PREFIX_NAME_SUFFIX( 0x1005BD ),
	
	/**
	 * ~1_NUMBER~ ~2_ITEMNAME~
	 */
	ITEM_NAME( 1050039 );
	
	private int code;
	
	private Clilocs(int code) {
		this.code = code;
	}
	
	@Override
	public int getCode() {
		return code;
	}

}
