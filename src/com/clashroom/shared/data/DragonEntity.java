package com.clashroom.shared.data;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.clashroom.server.QueryUtils;

@PersistenceCapable
public class DragonEntity {
	
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
	
	public UserEntity getPlayer(PersistenceManager pm) {
		return QueryUtils.queryUnique(pm, UserEntity.class, "dragonId == %s", id);
	}
	
	public DragonEntity(String name) {
		this.name = name;
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

	public int getDragonClassId() {
		return dragonClassId;
	}

	public void setDragonClassId(int dragonClassId) {
		this.dragonClassId = dragonClassId;
	}

	public Long getId() {
		return id;
	}
	
	
}
