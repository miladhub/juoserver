package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

import net.sf.juoserver.api.Skill;

public class SkillUpdate extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0x3A;
	public static enum SkillUpdateType {
		FullList(0x00, false, true),
		SingleUpdate(0xFF, false, false),
		FullListWithCap(0x02, true, true),
		SingleUpdateWithCap(0xDF, true, false);
		/**
		 * 1-based skill ID.
		 */
		private int code;
		private boolean capped;
		private boolean fullList;
		private SkillUpdateType(int code, boolean capped, boolean fullList) {
			this.code = code;
			this.capped = capped;
			this.fullList = fullList;
		}
		public byte getCode() {
			return (byte) code;
		}
		public boolean isCapped() {
			return capped;
		}
		public boolean isFullList() {
			return fullList;
		}
	}
	private SkillUpdateType skillUpdateType;
	private Skill[] skills;
	public SkillUpdate(SkillUpdateType skillUpdateType, Skill... skills) {
		super(CODE, computeLength(skillUpdateType, skills));
		this.skillUpdateType = skillUpdateType;
		this.skills = skills;
	}
	private static int computeLength(SkillUpdateType skillUpdateType, Skill... skills) {
		return 4 + skills.length * (7 + (skillUpdateType.isCapped() ? 2 : 0)) +
		(skillUpdateType.isFullList() ? 2 : 0); // Null terminated
	}
	@Override
	public ByteBuffer encode() {
		ByteBuffer bb = super.encode();
		bb.putShort((short) getLength());
		bb.put(skillUpdateType.getCode());
		for (Skill s : skills) {
			bb.putShort((short) s.getSkillId());
			bb.putShort((short) (s.getValue() * 10)); // Value
			bb.putShort((short) (s.getUnmodifiedValue() * 10)); // Unmodified value
			bb.put((byte) s.getLockFlag().getCode());
			if (skillUpdateType.isCapped()) {
				bb.putShort((short) (s.getCap() * 10));
			}
		}
		if (skillUpdateType.isFullList()) {
			bb.putShort((short) 0); // Null terminated
		}
		return bb;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((skillUpdateType == null) ? 0 : skillUpdateType.hashCode());
		result = prime * result + Arrays.hashCode(skills);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillUpdate other = (SkillUpdate) obj;
		if (skillUpdateType != other.skillUpdateType)
			return false;
		if (!Arrays.equals(skills, other.skills))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SkillUpdate [skillUpdateType=" + skillUpdateType + ", skills="
				+ Arrays.toString(skills) + "]";
	}
	
}
