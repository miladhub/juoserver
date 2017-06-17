package net.sf.juoserver.api;

public enum MessageType implements Coded {
	Regular(0x00),
	System(0x01),
	Emote(0x02),
	Label(0x06),
	Mobile(0x07),
	Whisper(0x08),
	Yell(0x09),
	Spell(0x0A),
	Guild(0x0D),
	Alliance(0x0E),
	Command(0x0F);
	MessageType(int code) {
		this.code = code;
	}
	private int code;
	@Override
	public int getCode() {
		return code;
	}
}