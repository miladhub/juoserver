package net.sf.juoserver.protocol;

import java.nio.ByteBuffer;

import net.sf.juoserver.api.Decodable;
import net.sf.juoserver.api.SkillLockFlag;

@Decodable(code = SkillLock.CODE)
public class SkillLock extends AbstractMessage {
	private static final long serialVersionUID = 1L;
	protected static final int CODE = 0x3A;
	/**
	 * 0-based skill ID.
	 */
	private short skillId;
	private SkillLockFlag skillLockFlag;
	public SkillLock(byte[] contents) {
		super(CODE, 6);
		ByteBuffer bb = wrapContents(contents);
		bb.getShort(); // Length (6)
		skillId = bb.getShort();
		skillLockFlag = EnumUtils.byCode(bb.get(), SkillLockFlag.class);
	}
	public short getSkillId() {
		return skillId;
	}
	public SkillLockFlag getSkillLockFlag() {
		return skillLockFlag;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + skillId;
		result = prime * result
				+ ((skillLockFlag == null) ? 0 : skillLockFlag.hashCode());
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
		SkillLock other = (SkillLock) obj;
		if (skillId != other.skillId)
			return false;
		if (skillLockFlag != other.skillLockFlag)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SkillLock [skillId=" + skillId + ", skillLockFlag="
				+ skillLockFlag + "]";
	}
}
