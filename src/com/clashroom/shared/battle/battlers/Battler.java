package com.clashroom.shared.battle.battlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.buff.Buff;
import com.clashroom.shared.battle.buff.Buff.Stat;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.skills.AttackSkill;
import com.clashroom.shared.battle.skills.ActiveSkill;
import com.clashroom.shared.battle.skills.ActiveSkill.Target;
import com.clashroom.shared.battle.skills.Skill;
import com.google.gwt.dev.util.collect.HashMap;

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

	public ArrayList<Buff> buffs = new ArrayList<Buff>();

	protected transient LinkedList<Battler> tempBattlers = new LinkedList<Battler>();
	protected transient LinkedList<ActiveSkill> tempSkills = new LinkedList<ActiveSkill>(); 
	
	public int getStrength() {
		return modify(strength, Stat.Str);
	}
	
	public int getAgility() {
		return modify(agility, Stat.Agi);
	}

	public int getIntelligence() {
		return modify(intelligence, Stat.Int);
	}

	public int getMaxHp() {
		return modify(maxHp, Stat.MaxHp);
	}

	public int getMaxMp() {
		return modify(maxMp, Stat.MaxMp);
	}

	public transient Object tag;

	public boolean isDead() {
		return hp == 0;
	}

	@SuppressWarnings("unchecked")
	public <T> T getTag() {
		return (T)tag;
	}

	public Battler() {
		skills.add(new AttackSkill());
	}

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
	
	public double getSpellModifier() {
		return modify(getSpellModifier(getIntelligence(), level), Stat.Spell);
	}
	
	public static double getSpellModifier(int intelligence, int level) {
		//return 1 + Math.pow(intelligence, 1.75) / (150 * (level + 10));
		return getDerivedStatCurve(1, 3.5, intelligence, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}
	
	public double getMeleeModifier() {
		return modify(getMeleeModifier(getStrength()), Stat.Melee);
	}
	
	public static double getMeleeModifier(int strength) {
		return getDerivedStatCurve(1, 5, strength, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}
	
	public double getDodgeChance() {
		return modify(getDodgeChance(getAgility(), level), Stat.Dodge);
	}
	
	public static double getDodgeChance(int agility, int level) {
		//return 0.01 + Math.pow(agility, 1.6) / (2500 * (level + 10));
		return getDerivedStatCurve(0.05, 0.6, agility, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}
	
	public double getCriticalChance() {
		return modify(getCriticalChance(getAgility(), level), Stat.Crit);
	}
	
	public double getCriticalChance(int agility, int level) {
		//return 0.01 + Math.pow(agility, 1.55) / (2500 * (level + 10));
		return getDerivedStatCurve(0.03, 0.5, agility, DragonClass.MAX_STAT_GAIN_PER_LEVEL);
	}

	private static double getDerivedStatCurve(double min, double max, int stat, int maxStatGainPerLvl) {
		//by lvl ~8 at max stat gain, reaches 1/5 of max
		//by lvl 15 at max stat gain, reaches 1/3 of max
		//by lvl 30 at max stat gain, reaches 1/2 of max
		//by lvl 60 at max stat gain, reaches 2/3 of max
		//by lvl 90 at max stat gain, reaches 3/4 of max
		return min + stat * (max-min) / (stat + maxStatGainPerLvl * 30);  
	}
	
	public int getExpReward() {
		return (int)(23 * expFactor * (1 + (level - 1) * 0.3));
	}

	protected void generateMaxHP() {
		maxHp = 100 + (10 * level) + (10 * strength);
	}

	protected void generateMaxMP() {
		maxMp = 100 + (5 * level) + (5 * intelligence);
	}
	
	private int modify(int value, Stat stat) {
		return (int)modify((double)value, stat);
	}
	
	private double modify(double value, Stat stat) {
		for (Buff buff : buffs) {
			value *= buff.getModifier(stat).getFactor();
		}
		for (Buff buff : buffs) {
			value += buff.getModifier(stat).getPlus();
		}
		return value;
	}

	protected int getStatCurve(int level, int minGain, int maxGain) {
		return (int)((Math.random() * (maxGain - minGain) + minGain) * (level + 0));
	}

	protected Battler selectTarget(List<Battler> targets, ActiveSkill skill, Random random) {
		if (targets.size() == 0) return null;
		int index;
		if (targets.size() > 2 && skill.getTarget() == Target.Splash) {
			index = 1 + random.nextInt(targets.size() - 2);
		} else {
			index = random.nextInt(targets.size());
		}
		return targets.get(index);
	}

	public Battler selectEnemyTarget(List<Battler> targets, ActiveSkill skill, Random random) {
		return selectTarget(targets, skill, random);
	}

	public Battler selectAllyTarget(List<Battler> targets, ActiveSkill skill, Random random) {
		tempBattlers.clear();
		for (Battler battler : targets) {
			if (battler.hp < battler.maxHp) tempBattlers.add(battler);
		}
		return selectTarget(tempBattlers, skill, random);
	}

	public boolean skillValid(ActiveSkill skill, boolean selfHealed, boolean alliesHealed) {
		if (skill.targetsAllies()) {
			if (skill.getTarget() == Target.Self) {
				return !selfHealed;
			} else {
				return !alliesHealed;
			}
		}
		return true;
	}

	public ActiveSkill selectSkill(Random random, List<Battler> allies, List<Battler> enemies) {
		tempSkills.clear();
		boolean alliesHealed = true;
		for (Battler battler : allies) {
			if (battler.hp != battler.maxHp) alliesHealed = false;
		}
		boolean selfHealed = hp == maxHp;
		for (Skill skill : skills) {
			if (skill.isActive()) {
				ActiveSkill activeSkill = skill.asActive();
				if (activeSkill.getMpCost() <= mp && skillValid(activeSkill, selfHealed, alliesHealed)) {
					tempSkills.add(activeSkill);
				}
			}
		}
		//if (tempSkills.size() == 0) return null;
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
	
	protected abstract Battler copyChild();
}
