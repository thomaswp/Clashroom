package com.clashroom.shared.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.clashroom.shared.battle.dragons.DragonClass;

/**
 *  An entity representing a user's Dragon. It is embedded in the 
 *  {@link UserEntity}, meaning they are combined when stored in the
 *  datastore. This allows them to be tied together, but they can exist
 *  as separate objects for good Object-Oriented practice.
 */
@SuppressWarnings("serial")
@PersistenceCapable
@EmbeddedOnly
public class DragonEntity implements Serializable {
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private int experience;
	
	@Persistent
	private int level = 1;
	
	@Persistent
	private double strength, intelligence, agility, maxHp, maxMp; 
	
	@Persistent
	private int dragonClassId;
	
	@Persistent
	private List<Integer> skills = new LinkedList<Integer>();
	
	/**
	 * Get the {@link DragonClass} of this dragon,
	 * using it's {@link DragonEntity#dragonClassId}.
	 */
	public DragonClass getDragonClass() {
		return DragonClass.getById(dragonClassId);
	}

	/**
	 * Adds experience to this Dragon, and handles any leveling up
	 * or stat gains from the level up.
	 * @param exp The experience to gain
	 */
	public void addExp(int exp) {
		DragonClass dragonClass = getDragonClass();
		this.experience += exp;
		while (this.experience >= DragonClass.getNextLevelExp(level)) {
			this.experience -= DragonClass.getNextLevelExp(level);
			this.level++;
			dragonClass.levelUp(this);
		}
		setExperience(experience);
		setLevel(level);
	}
	
	public DragonEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

	public double getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(double intelligence) {
		this.intelligence = intelligence;
	}

	public double getAgility() {
		return agility;
	}

	public void setAgility(double agility) {
		this.agility = agility;
	}
	
	public double getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(double maxHp) {
		this.maxHp = maxHp;
	}

	public double getMaxMp() {
		return maxMp;
	}

	public void setMaxMp(double maxMp) {
		this.maxMp = maxMp;
	}

	/**
	 * Gets the id of the {@link DragonClass} of this
	 * Dragon.
	 * @return The id
	 */
	public int getDragonClassId() {
		return dragonClassId;
	}

	public void setDragonClassId(int dragonClassId) {
		this.dragonClassId = dragonClassId;
	}

	public Long getId() {
		return id;
	}

	public List<Integer> getSkills() {
		return skills;
	}

	public void setSkills(List<Integer> skills) {
		if (skills == null) skills = new LinkedList<Integer>();
		this.skills = skills;
	}	
}
