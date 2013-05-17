package com.clashroom.shared.battle.skills;

import java.io.Serializable;
import java.util.HashMap;

import com.clashroom.shared.Constant;
import com.clashroom.shared.battle.battlers.Battler;

/**
 * Represents a skill on {@link Battler} can use to damage/heal/buff another
 * {@link Battler}, or which can passively affect the {@link Battle}. All attacks,
 * even the basic {@link AttackSkill} are skills.
 * <p />
 * <b>Any new Skills need to register themselves in the {@link Skill#skills} array
 * in order to have an id. This is important!</b> 
 */
public abstract class Skill implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Currently not being used - this allows
	 * a {@link Skill} to be affiliated with a 
	 * specific stat. Instead, an {@link ActiveSkill} can
	 * override {@link ActiveSkill#getAttackModifier(Battler)}
	 * to change how the {@link Battler} alters its attack damage.
	 */
	public enum Attribute {
		Strength, Agility, Intelligence, None
	}
	
	//<-- Don't change the order from here
	private static int nextId = 0;
	protected int id;

	private static HashMap<Class<?>, Integer> skillIds = 
			new HashMap<Class<?>, Integer>();
	
	/**
	 * Each skill must be included in this array
	 * to have a unique id by which it can be recreated.
	 */
	private static Skill[] skills = new Skill[] {
		new AttackSkill(),
		new FireballSkill(),
		new FireBreathSkill(),
		new HealSkill(),
		new EggShellSkill(),
		new FogSkill(),
		new StormSkill(),
		new LightningSkill(),
		new LionFireSkill(),
		new BlessingSkill(),
		new BodySlamSkill(),
		new CurseSkill(),
		new EnrageSkill(),
		new HurlBoulderSkill()
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

	/**
	 * Gets the name of the icon for this spell. Use the
	 * {@link Constant#IMG_ICON} directory when loading this icon.
	 */
	public String getIcon() {
		return icon;
	}

	public Attribute getAttribute() {
		return attribute;
	}
	
	/**
	 * Should return the cost, in skill points,
	 * of learning this skill.
	 */
	public abstract int getSkillPointCost();
	
	/**
	 * Should return a brief description of this skill.
	 */
	public abstract String getDescription();

	protected Skill(String name, String icon, Attribute attribute) {
		
		this.id = generateId();
		
		this.name = name;
		this.icon = icon;
		this.attribute = attribute;
	}
	
	/**
	 * Gets a Skill by its id. This Skill will not necessarily
	 * by a unique object, but since Skills are largely immutable,
	 * this should not be a problem.
	 * 
	 * @param id The id of the skill to retrieve
	 * @return The Skill
	 */
	public static Skill getById(int id) {
		return skills[id];
	}
	
	/**
	 * Gets a Skill by its {@link Class}. This Skill will not necessarily
	 * by a unique object, but since Skills are largely immutable,
	 * this should not be a problem.
	 * 
	 * @param clazz The Skill's class
	 * @return The Skill
	 */
	public static Skill getByClass(Class<? extends ActiveSkill> clazz) {
		Integer id = skillIds.get(clazz);
		if (id == null) return null;
		return skills[id];
	}
	
	//Ensures every Skill has a unique id, and that its Class it registered
	//with that Skill
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
	
	/**
	 * Returns true if this is an {@link ActiveSkill}.
	 */
	public boolean isActive() {
		return this instanceof ActiveSkill;
	}
	
	/**
	 * Returns this Skill, cast to an {@link ActiveSkill}
	 */
	public ActiveSkill asActive() {
		return (ActiveSkill)this;
	}

	/**
	 * Returns this Skill, cast to a {@link PassiveSkill}
	 */
	public PassiveSkill asPassive() {
		return (PassiveSkill)this;
	}
	
	/**
	 * Override this method to ensure the Skill does not show
	 * up on the list of a Dragon's skills. This is mainly for
	 * use by {@link AttackSkill}, which should not show up.
	 */
	public boolean showSkill() {
		return true;
	}

	/**
	 * Gets the value for this Skill's {@link Attribute} in the given {@link Battler}.
	 * Currently not being used.
	 * @param battler The Battler
	 * @param attribute The Attribute
	 * @return The value
	 */
	protected static int getAttribute(Battler battler, Attribute attribute) {
		switch (attribute) {
		case Strength: return battler.getStrength();
		case Agility: return battler.getAgility();
		case Intelligence: return battler.getIntelligence();
		case None: return 0;
		}
		return 0;
	}
}
