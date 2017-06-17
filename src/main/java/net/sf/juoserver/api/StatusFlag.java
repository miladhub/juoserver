package net.sf.juoserver.api;

public enum StatusFlag implements Coded {
	None(0), T2A(1), UOR(3), AOS(4), UOML(5); //, UOKR(6) --> not supported
	private int code;
	private StatusFlag(int code) {
		this.code = code;
	}
	@Override
	public int getCode() {
		return code;
	}
}
