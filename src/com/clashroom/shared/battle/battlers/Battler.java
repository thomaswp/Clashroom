package com.clashroom.shared.battle.battlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.clashroom.client.battle.BattlePage;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.buffs.Buff;
import com.clashroom.shared.battle.buffs.Buff.Stat;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.skills.AttackSkill;
import com.clashroom.shared.battle.skills.ActiveSkill;
import com.clashroom.shared.battle.skills.ActiveSkill.Target;
import com.clashroom.shared.battle.skills.Skill;
import com.google.gwt.dev.util.collect.HashMap;

/**
 * Represents one fighter in a {@link Battle}. Like Battles,
 * these are somewhat disposable classes and can be modified in the Battle.
 * The {@link BattleFactory} holds the original copies, and will recreate them
 * when the Battle is recreated.
 */
public abstract class Battler implements Serializable {
	private static final long serialVersionUID = 1L;

	public String name;
	public int level;
	public String description;
	protected int strength, agility, intelligence;
	protected int maxHp, maxMp;
	public int hp, mp;
	public String image;
	public boolean teamA;
	public ArrayList<Skill> skills = new ArrayList<Skill>();
	public double expFactor = 1;

	//Buffs currently affacting this Battler
	public ArrayList<Buff> buffs = new ArrayList<Buff>();

	//Transient because they don't need to be serializaed - they are used for temp calculations
	protected transient LinkedList<Battler> tempBattlers = new LinkedList<Battler>();
	protected transient LinkedList<ActiveSkill> tempSkills = new LinkedList<ActiveSkill>(); 
	
	/**
	 * Returns this Battler's strength, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public int getStrength() {
		return modify(strength, Stat.Str);
	}
	
	/**
	 * Returns this Battler's agility, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public int getAgility() {
		return modify(agility, Stat.Agi);
	}

	/**
	 * Returns this Battler's intelligence, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public int getIntelligence() {
		return modify(intelligence, Stat.Int);
	}

	/**
	 * Returns this Battler's max hp, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public int getMaxHp() {
		return modify(maxHp, Stat.MaxHp);
	}

	/**
	 * Returns this Battler's max mp, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public int getMaxMp() {
		return modify(maxMp, Stat.MaxMp);
	}

	/** Tag to be used by {@link BattlePage} or any other class for temporary object association */
	public transient Object tag;

	public boolean isDead() {
		return hp == 0;
	}

	/**
	 * Gets this Battler's tag and casts is as implied
	 */
	@SuppressWarnings("unchecked")
	public <T> T getTag() {
		return (T)tag;
	}

	public Battler() {
		skills.add(new AttackSkill());
	}

	/**
	 * Resets a Battler for the beginning of battle
	 */
	public void setup() {
		buffs.clear();
		for (Skill skill : skills) {
			if (!skill.isActive()) {
				buffs.addAll(skill.asPassive().getBuffs());
			}
		}
		hp = getMaxHp();
		mp = getMaxMp();
		tempBattlers = new LinkedList<Battler>();
		tempSkills = new LinkedList<ActiveSkill>();
	}
	
	/**
	 * Returns this Battler's spell modifier, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public double getSpellModifier() {
		return modify(getSpellModifier(getIntelligence(), level), Stat.Spell);
	}
	
	/**
	 * Gets a base spell damage modifier for a {@link Battler} with the given
	 * intelligence and level.
	 */
	public static double getSpellModifier(int intelligence, int level) {
		//return 1 + Math.pow(intelligence, 1.75) / (150 * (level + 10));
		return getDerivedStatCurve(1, 3.5, intelligence, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}
	
	/**
	 * Returns this Battler's melee modifier, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public double getMeleeModifier() {
		return modify(getMeleeModifier(getStrength()), Stat.Melee);
	}
	
	/**
	 * Gets a base melee damage modifier for a {@link Battler} with the given
	 * strength.
	 */
	public static double getMeleeModifier(int strength) {
		return getDerivedStatCurve(1, 5, strength, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}
	
	/**
	 * Returns this Battler's dodge chance, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public double getDodgeChance() {
		return modify(getDodgeChance(getAgility(), level), Stat.Dodge);
	}
	
	/**
	 * Gets the dodge change for a {@link Battler} with the given
	 * agility and level.
	 */
	public static double getDodgeChance(int agility, int level) {
		//return 0.01 + Math.pow(agility, 1.6) / (2500 * (level + 10));
		return getDerivedStatCurve(0.05, 0.6, agility, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}
	
	/**
	 * Returns this Battler's critical chance, modified by any {@link Buff}s
	 * affecting this Battler.
	 */
	public double getCriticalChance() {
		return modify(getCriticalChance(getAgility(), level), Stat.Crit);
	}
	
	/**
	 * Gets the critical chance for a {@link Battler} with the given
	 * agility and level.
	 */
	public static double getCriticalChance(int agility, int level) {
		//return 0.01 + Math.pow(agility, 1.55) / (2500 * (level + 10));
		return getDerivedStatCurve(0.03, 0.5, agility, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}

	/**
	 * Gets the value of a stat curve, based on the min value that stat can have, the max value it can have,
	 * the current stat of the Battler and the maximum amount of that stat that can be gained per level 
	 * @param min The smallest amount of this stat a Battler should be able to have
	 * @param max The max amount of the stat a Battler should be able to have
	 * @param stat The amount of this stat the Battler actually has
	 * @param maxStatGainPerLvl The max amount of this stat that can be gained each level
	 * @return The calculated value of this stat for this Battler
	 */
	private static double getDerivedStatCurve(double min, double max, int stat, int maxStatGainPerLvl) {
		//by lvl ~8 at max stat gain, reaches 1/5 of max
		//by lvl 15 at max stat gain, reaches 1/3 of max
		//by lvl 30 at max stat gain, reaches 1/2 of max
		//by lvl 60 at max stat gain, reaches 2/3 of max
		//by lvl 90 at max stat gain, reaches 3/4 of max
		return min + stat * (max-min) / (stat + maxStatGainPerLvl * 30);  
	}
	
	/**
	 * Gets the amount of exp fighting this Battler should give
	 * @return
	 */
	public int getExpReward() {
		return (int)(23 * expFactor * (1 + (level - 1) * 0.3));
	}

	/**
	 * For non-dragon Battlers, gives a way to generate MaxHp off of
	 * strength
	 */
	protected void generateMaxHP() {
		maxHp = 100 + (10 * level) + (10 * strength);
	}

	/**
	 * For non-dragon Battlers, gives a way to generate basic str/agi/int curves
	 * based on level.
	 */
	protected int getStatCurve(int level, int minGain, int maxGain) {
		return (int)((Math.random() * (maxGain - minGain) + minGain) * (level + 0));
	}

	/**
	 * For non-dragon Battlers, gives a way to generate MaxMp off of
	 * strength
	 */
	protected void generateMaxMP() {
		maxMp = 100 + (5 * level) + (5 * intelligence);
	}
	
	/**
	 * See {@link Battler#modify(double, Stat)}
	 */
	private int modify(int value, Stat stat) {
		return (int)modify((double)value, stat);
	}
	
	/**
	 * Returns a modified value for this stat, given this Battler's {@link Buff}s.
	 * @param value The value to modify
	 * @param stat The Stat being modified
	 * @return The modified stat value
	 */
	private double modify(double value, Stat stat) {
		for (Buff buff : buffs) {
			value *= buff.getModifier(stat).getFactor();
		}
		for (Buff buff : buffs) {
			value += buff.getModifier(stat).getPlus();
		}
		return value;
	}

	/**
	 * Has this Battler select a target for the given Skill.
	 * @param targets The possible targets to attack
	 * @param skill The Skill to attack with
	 * @param random The seeded Random to use for calculations
	 * @return The Battler to target with the given Skill
	 */
	protected Battler selectTarget(List<Battler> targets, ActiveSkill skill, Random random) {
		if (targets.size() == 0) return null;
		int index;
		if (targets.size() > 2 && skill.getTarget() == Target.Splash) {
			//target the middle Battlers when using splash damage
			index = 1 + random.nextInt(targets.size() - 2);
		} else {
			index = random.nextInt(targets.size());
		}
		return targets.get(index);
	}

	/**
	 * Has this Battler select an enemy target for the given Skill.
	 * @param targets The possible targets to attack
	 * @param skill The Skill to attack with
	 * @param random The seeded Random to use for calculations
	 * @return The Battler to target with the given Skill
	 */
	public Battler selectEnemyTarget(List<Battler> targets, ActiveSkill skill, Random random) {
		return selectTarget(targets, skill, random);
	}

	/**
	 * Has this Battler select an ally target for the given Skill.
	 * @param targets The possible targets to attack
	 * @param skill The Skill to attack with
	 * @param random The seeded Random to use for calculations
	 * @return The Battler to target with the given Skill
	 */
	public Battler selectAllyTarget(List<Battler> targets, ActiveSkill skill, Random random) {
		tempBattlers.clear();
		for (Battler battler : targets) {
			//only try to heal injured Battlers
			if (battler.hp < battler.maxHp) tempBattlers.add(battler);
		}
		return selectTarget(tempBattlers, skill, random);
	}

	/**
	 * Returns true if this Battler can use the given Skill at this time
	 * @param skill The skill
	 * @param selfHealed Is this Battler at full health
	 * @param alliesHealed Are this Battler's allies at full health
	 */
	protected boolean skillValid(ActiveSkill skill, boolean selfHealed, boolean alliesHealed) {
		if (skill.targetsAllies()) {
			if (skill.getTarget() == Target.Self) {
				return !selfHealed;
			} else {
				return !alliesHealed;
			}
		}
		return true;
	}

	/**
	 * Selects a valid skill for this Battler to use this turn
	 * @param random A seeded Random to use for caluclations
	 * @param allies This Battler's allies
	 * @param enemies This Battler's enemies
	 * @return The ActiveSkill to use
	 */
	public ActiveSkill selectSkill(Random random, List<Battler> allies, List<Battler> enemies) {
		tempSkills.clear();
		boolean alliesHealed = true;
		for (Battler battler : allies) {
			if (battler.hp != battler.maxHp) alliesHealed = false;
		}
		boolean selfHealed = hp == maxHp;
		for (Skill skill : skills) {
			//Can't use passive skills
			if (skill.isActive()) {
				ActiveSkill activeSkill = skill.asActive();
				//Make sure there's enough mp and there's a valid target for this skill
				if (activeSkill.getMpCost() <= mp && skillValid(activeSkill, selfHealed, alliesHealed)) {
					tempSkills.add(activeSkill);
				}
			}
		}
		return tempSkills.get(random.nextInt(tempSkills.size()));
	}

	@Override
	public String toString() {
		return Formatter.format("%s %d/%dhp", name, hp, maxHp);
	}

	public void setLevel(int newLevel) {
		this.level = newLevel;
		this.description = getDescription();
	}

	protected abstract String getDescription();

	/**
	 * Gets a field-for-field copy fo this Battler.
	 * Sub-classes must overwrite the {@link Battler#copyChild()} 
	 * method where they should copy any new fields.
	 * @return The copy
	 */
	public final Battler copy() {
		Battler battler = copyChild();
		battler.name = name;
		battler.image = image;
		battler.level = level;
		battler.maxHp = maxHp;
		battler.maxMp = maxMp;
		battler.strength = strength;
		battler.agility = agility;
		battler.intelligence = intelligence;
		battler.description = description;
		battler.skills.addAll(skills);
		return battler;
	}
	
	/**
	 * Get a copy of this sub-class, assigning
	 * any new fields from this class to the copy.
	 * @return The copy
	 */
	protected abstract Battler copyChild();
}
