package net.sf.juoserver.files.mondainslegacy;

import net.sf.juoserver.api.SkillsFileEntry;

class MondainsLegacySkillsFileEntry implements SkillsFileEntry {
	private String skillName;
	private boolean hasButton;

	public MondainsLegacySkillsFileEntry(String skillName, boolean hasButton) {
		super();
		this.skillName = skillName;
		this.hasButton = hasButton;
	}

	@Override
	public String getSkillName() {
		return skillName;
	}

	@Override
	public boolean isHasButton() {
		return hasButton;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((skillName == null) ? 0 : skillName.hashCode());
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
		MondainsLegacySkillsFileEntry other = (MondainsLegacySkillsFileEntry) obj;
		if (skillName == null) {
			if (other.skillName != null)
				return false;
		} else if (!skillName.equals(other.skillName))
			return false;
		return true;
	}
}
