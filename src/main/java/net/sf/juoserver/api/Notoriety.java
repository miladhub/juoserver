package net.sf.juoserver.api;

public enum Notoriety implements Coded {
	/**
	 * Across server line.
	 */
	Invalid,
	/**
	 * Blue.
	 */
	Innocent,
	/**
	 * Ally (green).
	 */
	Guilded,
	/**
	 * Gray.
	 */
	AttackableNotCriminal,
	/**
	 * Gray.
	 */
	Criminal,
	/**
	 * Orange.
	 */
	Enemy,
	/**
	 * Red.
	 */
	Murderer,
	/**
	 * Translucent, like 0x4000 hue.
	 */
	Unknown;
	@Override
	public int getCode() {
		return ordinal();
	}
}
