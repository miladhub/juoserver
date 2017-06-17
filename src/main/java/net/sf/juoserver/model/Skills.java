package net.sf.juoserver.model;

import net.sf.juoserver.api.Coded;
import net.sf.juoserver.files.CustomizationHelper;

/**
 * Default skill set.
 * <p/>
 * Skills can be totally customized as long as you provide the
 * <tt>idx</tt> and <tt>mul</tt> files as can be loaded via
 * method {@link CustomizationHelper#getSkillIdByName(String)}.
 */
public enum Skills implements Coded {
	
	Alchemy, Anatomy, AnimalLore, ItemIdentify, ArmsLore, Parrying, Begging, Blacksmithing, 
	Bowcraft, Peacemaking, Camping, Carpentry, Cartography, Cooking, DetectingHidden, Enticement,
	EvaluateIntelligence, Healing, Fishing, ForensicEvaluation, Herding, Hiding, Provocation,
	Inscription, Lockpicking, Magery, MagicResistance, Tactics, Snooping, Musicianship, Poisoning, Archery,
	SpiritSpeaking, Stealing, Tailoring, AnimalTaming, TasteIdentification, Tinkering, Tracking, Veterinary,
	Swordsmanship, Macefighting, Fencing, Wrestling, Lumberjacking, Mining, Meditation, Stealth, RemoveTraps,
	Nrcromancy, Focus, WarriorMagery, Bushido, Ninjitsu, SpellWeaving;
	
	/**
	 * Retrieves the 1-based skill ID.
	 * @return the 1-based skill ID
	 */
	@Override
	public int getCode() {
		return ordinal() + 1;
	}

}
