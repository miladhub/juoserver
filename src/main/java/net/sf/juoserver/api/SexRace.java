package net.sf.juoserver.api;

public enum SexRace implements Coded {
	MaleHuman, FemaleHuman, MaleElf, FemaleElf;
	@Override
	public int getCode() {
		return ordinal();
	}
}
