package com.clashroom.shared.battle.skills;

import java.io.Serializable;
import java.util.HashMap;
import com.clashroom.shared.battle.battlers.Battler;

public abstract class Skill implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public enum Attribute {
		Strength, Agility, Intelligence
	}
	
	//<-- Don't change the order from here
	private static int nextId = 0;
	protected int id;

	private static HashMap<Class<?>, Integer> skillIds = 
			new HashMap<Class<?>, Integer>();
	
	private static ActiveSkill[] skills = new ActiveSkill[] {
		new AttackSkill(),
		new FireballSkill(),
		new FireBreathSkill(),
		new HealSkill(),
		//Add new skills down here
	};
	//--> to here
	
	protected String name;
	protected String icon;
	protected Attribute attribute;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public Attribute getAttribute() {
		return attribute;
	}
	
	public abstract int getSkillPointCost();
	
	public abstract String getDescription();

	protected Skill(String name, String icon, Attribute attribute) {
		
		this.id = generateId();
		
		this.name = name;
		this.icon = icon;
		this.attribute = attribute;
	}
	
	public static Skill getById(int id) {
		return skills[id];
	}
	
	public static Skill getByClass(Class<? extends ActiveSkill> clazz) {
		Integer id = skillIds.get(clazz);
		if (id == null) return null;
		return skills[id];
	}
	
	private int generateId() {
		synchronized (skillIds) {
			Class<?> myClass = getClass();
			if (skillIds.containsKey(myClass)) {
				return skillIds.get(myClass);
			} else {
				int myId = nextId++;
				skillIds.put(myClass, myId);
				return myId;
			}
		}
	}
	
	public boolean isActive() {
		return this instanceof ActiveSkill;
	}
	
	public ActiveSkill asActive() {
		return (ActiveSkill)this;
	}
	
	public boolean showSkill() {
		return true;
	}

	protected static int getAttribute(Battler battler, Attribute attribute) {
		switch (attribute) {
		case Strength: return battler.strength;
		case Agility: return battler.agility;
		case Intelligence: return battler.intelligence;
		}
		return 0;
	}
}
