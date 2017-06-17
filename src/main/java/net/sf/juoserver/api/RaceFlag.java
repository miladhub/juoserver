package net.sf.juoserver.api;

public enum RaceFlag implements Coded {
	Human(1), Elf(2), Gargoyle(3);
	private int code;
	private RaceFlag(int code) {
		this.code = code;
	}
	@Override
	public int getCode() {
		return code;
	}
}
