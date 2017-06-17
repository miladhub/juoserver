package net.sf.juoserver.model;

import java.util.EnumSet;

public enum ClientFlag {
	T2A(0x00),
	Renaissance(0x01),
	ThirdDawn(0x02),
	LBR(0x04),
	AOS(0x08),
	SE(0x10),
	SA(0x20),
	UO3D(0x40),
//	reserved(0x80),
	TheeDClient(0x100);
	private int flag;
	private ClientFlag(int flag) {
		this.flag = flag;
	}
	public int getFlag() {
		return flag;
	}
	public static EnumSet<ClientFlag> parse(int clientFlag) {
		EnumSet<ClientFlag> flags = EnumSet.noneOf(ClientFlag.class);
		for (ClientFlag cf : values()) {
			if ((clientFlag & cf.getFlag()) == cf.getFlag()) {
				flags.add(cf);
			}
		}
		return flags;
	}
}
