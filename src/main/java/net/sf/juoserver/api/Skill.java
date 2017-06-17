package net.sf.juoserver.api;

public class Skill {
	private int skillId;
	private int value;
	private int unmodifiedValue;
	private int cap;
	private SkillLockFlag lockFlag;

	public Skill(Coded skillCode, int value, int unmodifiedValue, int cap,
			SkillLockFlag lockFlag) {
		super();
		this.skillId = skillCode.getCode();
		this.value = value;
		this.unmodifiedValue = unmodifiedValue;
		this.lockFlag = lockFlag;
		this.cap = cap;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getValue() {
		return value;
	}

	public int getUnmodifiedValue() {
		return unmodifiedValue;
	}

	public int getCap() {
		return cap;
	}

	public SkillLockFlag getLockFlag() {
		return lockFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + skillId;
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
		Skill other = (Skill) obj;
		if (skillId != other.skillId)
			return false;
		return true;
	}
}

