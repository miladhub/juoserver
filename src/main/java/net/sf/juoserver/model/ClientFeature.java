package net.sf.juoserver.model;

public enum ClientFeature {
	None(0x00000),
	T2A(0x00001),
	UOR(0x00002),
	UOTD(0x00004),
	LBR(0x00008),
	AOS(0x00010),
	SixthCharacterSlot(0x20),	
	SE(0x00000040),
	ML(0x00000080),
	SeventhCharacterSlot(0x00001000),
	SA(0x00010000);
	short value;
	private ClientFeature(int value) {
		this.value = (short) value;
	}
	public short getValue() {
		return value;
	}
}
