package net.sf.juoserver.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.juoserver.api.CharacterStatus;
import net.sf.juoserver.api.Direction;
import net.sf.juoserver.api.Layer;
import net.sf.juoserver.api.Notoriety;
import net.sf.juoserver.api.RaceFlag;
import net.sf.juoserver.api.SexRace;
import net.sf.juoserver.api.Skill;
import net.sf.juoserver.api.SkillLockFlag;
import net.sf.juoserver.api.StatusFlag;
import net.sf.juoserver.api.Item;
import net.sf.juoserver.api.Mobile;

public class UOMobile implements Mobile {
	private int serialId;
	/**
	 * The body type.
	 */
	private int modelId = 0x190;
	private String name;
	private int currentHitPoints;
	private int maxHitPoints;
	private boolean nameChangeFlag;
	private StatusFlag statusFlag;
	private SexRace sexRace;
	private int strength;
	private int dexterity;
	private int intelligence;
	private int currentStamina;
	private int maxStamina;
	private int currentMana;
	private int maxMana;
	private int goldInPack;
	private int armorRating;
	private int weight;
	
	// statusFlag >= StatusFlag.UOML
	private int maxWeight;
	private RaceFlag raceFlag;
	
	private String title = "The Great";
	
	private Set<Skill> skills = new HashSet<Skill>( Arrays.asList(new Skill(Skills.Alchemy, 85, 80, 100, SkillLockFlag.Up),
					new Skill(Skills.Magery, 95, 90, 100, SkillLockFlag.Up),
					new Skill(Skills.Inscription, 95, 90, 100, SkillLockFlag.Up),
					new Skill(Skills.EvaluateIntelligence, 100, 100, 100, SkillLockFlag.Down),
					new Skill(Skills.ItemIdentify, 75, 70, 100, SkillLockFlag.Locked)) );
	
	private Map<Layer, Item> items;
	private int hue = 0x83EA;
	private int x = 0x02E8;
	private int y = 0x0877;
	private int z = 5;
	private Direction direction = Direction.Southeast;
	private boolean running;
	private Notoriety notoriety = Notoriety.Innocent;
	private CharacterStatus characterStatus = CharacterStatus.Normal;
	
	public UOMobile(int playerSerial, String playerName, int currentHitPoints,
			int maxHitPoints, boolean nameChangeFlag, StatusFlag statusFlag,
			SexRace sexRace, int strength, int dexterity, int intelligence,
			int currentStamina, int maxStamina, int currentMana, int maxMana,
			int goldInPack, int armorRating, int weight, int maxWeight,
			RaceFlag raceFlag) {
		super();
		this.serialId = playerSerial;
		this.name = playerName;
		this.currentHitPoints = currentHitPoints;
		this.maxHitPoints = maxHitPoints;
		this.nameChangeFlag = nameChangeFlag;
		this.statusFlag = statusFlag;
		this.sexRace = sexRace;
		this.strength = strength;
		this.dexterity = dexterity;
		this.intelligence = intelligence;
		this.currentStamina = currentStamina;
		this.maxStamina = maxStamina;
		this.currentMana = currentMana;
		this.maxMana = maxMana;
		this.goldInPack = goldInPack;
		this.armorRating = armorRating;
		this.weight = weight;
		this.maxWeight = maxWeight;
		this.raceFlag = raceFlag;
		
		items = new HashMap<Layer, Item>();
	}
	
	@Override
	public int getModelId() {
		return modelId;
	}
	
	@Override
	public int getSerialId() {
		return serialId;
	}
	
	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public int getZ() {
		return z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getCurrentHitPoints() {
		return currentHitPoints;
	}

	@Override
	public int getMaxHitPoints() {
		return maxHitPoints;
	}

	@Override
	public boolean isNameChangeFlag() {
		return nameChangeFlag;
	}

	@Override
	public StatusFlag getStatusFlag() {
		return statusFlag;
	}

	@Override
	public SexRace getSexRace() {
		return sexRace;
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public int getDexterity() {
		return dexterity;
	}

	@Override
	public int getIntelligence() {
		return intelligence;
	}

	@Override
	public int getCurrentStamina() {
		return currentStamina;
	}

	@Override
	public int getMaxStamina() {
		return maxStamina;
	}

	@Override
	public int getCurrentMana() {
		return currentMana;
	}

	@Override
	public int getMaxMana() {
		return maxMana;
	}

	@Override
	public int getGoldInPack() {
		return goldInPack;
	}

	@Override
	public int getArmorRating() {
		return armorRating;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public int getMaxWeight() {
		return maxWeight;
	}

	@Override
	public RaceFlag getRaceFlag() {
		return raceFlag;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Set<Skill> getSkills() {
		return skills;
	}

	@Override
	public Map<Layer, Item> getItems() {
		return items;
	}

	@Override
	public int getHue() {
		return hue;
	}

	@Override
	public Notoriety getNotoriety() {
		return notoriety;
	}

	@Override
	public CharacterStatus getCharacterStatus() {
		return characterStatus;
	}

	/**
	 * Updates this mobile's position according to their {@link #direction}.
	 */
	@Override
	public void moveForward() {
		switch (direction) {
		case North:
			--y;
			break;
		case Northeast:
			++x;
			--y;
			break;
		case East:
			++x;
			break;
		case Southeast:
			++x;
			++y;
			break;
		case South:
			++y;
			break;
		case Southwest:
			--x;
			++y;
			break;
		case West:
			--x;
			break;
		case Northwest:
			--x;
			--y;
			break;
		}
	}
	
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + serialId;
		return result;
	}
	
	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UOMobile other = (UOMobile) obj;
		if (serialId != other.serialId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getPrefixNameSuffix() {
		return " \t" + getName() + "\t ";
	}
	
	@Override
	public byte getDirectionWithRunningInfo() {
		return (byte) (direction.getCode() | (isRunning()? 0x80 : 0));
	}

	@Override
	public void setItemOnLayer(Layer layer, Item item) {
		items.put(layer, item);
	}
	
	@Override
	public Item getItemByLayer(Layer layer) {
		return items.get(layer);
	}

	@Override
	public boolean removeItem(Item item) {
		return items.remove(getLayer(item)) != null;
	}

	@Override
	public Layer getLayer(Item item) {
		for (Layer layer : Collections.unmodifiableSet(items.keySet())) {
			if (getItemByLayer(layer).equals(item)) {
				return layer;
			}
		}
		return null;
	}

	@Override
	public void emptyLayer(Layer layer) {
		removeItem(getItemByLayer(layer));
	}
	
	@Override
	public void setCharacterStatus(CharacterStatus characterStatus) {
		this.characterStatus = characterStatus;		
	}
	
}
