package net.sf.juoserver.api;

public enum SkillLockFlag implements Coded {
	Up(0), Down(1), Locked(2);
	
	private int code;

	private SkillLockFlag(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}
}
