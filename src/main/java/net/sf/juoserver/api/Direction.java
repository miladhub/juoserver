package net.sf.juoserver.api;

public enum Direction implements Coded {
	North, Northeast, East, Southeast, South, Southwest, West, Northwest;
	@Override
	public int getCode() {
		return ordinal();
	}
}
