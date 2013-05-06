package com.clashroom.shared.battle.battlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.skills.AttackSkill;
import com.clashroom.shared.battle.skills.ActiveSkill;
import com.clashroom.shared.battle.skills.FireballSkill;
import com.clashroom.shared.battle.skills.HealSkill;
import com.clashroom.shared.battle.skills.ActiveSkill.Target;
import com.clashroom.shared.battle.skills.Skill;

public abstract class Battler implements Serializable {
	private static final long serialVersionUID = 1L;

	public String name;
	public int level;
	public String description;
	public int strength, agility, intelligence;
	public int maxHp, maxMp;
	public int hp, mp;
	public String image;
	public boolean teamA;
	public ArrayList<Skill> skills = new ArrayList<Skill>();
	public double expFactor = 1;

	protected transient LinkedList<Battler> tempBattlers = new LinkedList<Battler>();
	protected transient LinkedList<ActiveSkill> tempSkills = new LinkedList<ActiveSkill>(); 

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
		for (Skill skill : skills) {
			if (!skill.isActive()) {
				skill.asPassive().applyBuff(this);
			}
		}
		hp = maxHp;
		mp = maxMp;
		tempBattlers = new LinkedList<Battler>();
		tempSkills = new LinkedList<ActiveSkill>();
	}

	public int getExpReward() {
		return (int)(23 * expFactor * (1 + (level - 1) * 0.3));
	}

	protected void generateMaxHP() {
		maxHp = level * 35 + strength * 23;
	}

	protected void generateMaxMP() {
		maxMp = level * 15 + intelligence * 8;
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
