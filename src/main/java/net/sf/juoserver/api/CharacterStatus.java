package net.sf.juoserver.api;

public enum CharacterStatus implements Coded {
	Normal(0x00), Unknown(0x01), CanAlterPaperdoll(0x02), Poisoned(0x04),
	GoldenHealth(0x08), Unknown0x10(0x10), Unknown0x20(0x20), WarMode(0x40);
	private int code;
	private CharacterStatus(int code) {
		this.code = code;
	}
	@Override
	public int getCode() {
		return code;
	}
}
