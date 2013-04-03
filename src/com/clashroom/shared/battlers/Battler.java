package com.clashroom.shared.battlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.skills.AttackSkill;
import com.clashroom.shared.skills.Skill;
import com.clashroom.shared.skills.Skill.Target;

public abstract class Battler implements Serializable{
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
	
	protected transient LinkedList<Battler> tempBattlers = new LinkedList<Battler>();
	protected transient LinkedList<Skill> tempSkills = new LinkedList<Skill>(); 
	
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
		hp = maxHp;
		mp = maxMp;
		tempBattlers = new LinkedList<Battler>();
		tempSkills = new LinkedList<Skill>();
	}
	
	protected void generateMaxHP() {
		maxHp = level * 35 + strength * 23;
	}
	
	protected void generateMaxMP() {
		maxMp = level * 15 + intelligence * 8;
	}
	
	protected int getStatCurve(int level, int minGain, int maxGain) {
		return (int)((Math.random() * (maxGain - minGain) + minGain) * (level + 3));
	}
	
	protected Battler selectTarget(List<Battler> targets, Skill skill, Random random) {
		if (targets.size() == 0) return null;
		int index;
		if (targets.size() > 2 && skill.target == Target.Splash) {
			index = 1 + random.nextInt(targets.size() - 2);
		} else {
			index = random.nextInt(targets.size());
		}
		return targets.get(index);
	}
	
	public Battler selectEnemyTarget(List<Battler> targets, Skill skill, Random random) {
		return selectTarget(targets, skill, random);
	}
	
	public Battler selectAllyTarget(List<Battler> targets, Skill skill, Random random) {
		tempBattlers.clear();
		for (Battler battler : targets) {
			if (battler.hp < battler.maxHp) tempBattlers.add(battler);
		}
		return selectTarget(tempBattlers, skill, random);
	}
	
	public boolean skillValid(Skill skill, boolean selfHealed, boolean alliesHealed) {
		if (skill.targetAllies) {
			if (skill.target == Target.Self) {
				return !selfHealed;
			} else {
				return !alliesHealed;
			}
		}
		return true;
	}
	
	public Skill selectSkill(Random random, List<Battler> allies, List<Battler> enemies) {
		tempSkills.clear();
		boolean alliesHealed = true;
		for (Battler battler : allies) {
			if (battler.hp != battler.maxHp) alliesHealed = false;
		}
		boolean selfHealed = hp == maxHp;
		for (Skill skill : skills) {
			if (skill.mpCost <= mp && skillValid(skill, selfHealed, alliesHealed)) {
				tempSkills.add(skill);
			}
		}
		//if (tempSkills.size() == 0) return null;
		return tempSkills.get(random.nextInt(tempSkills.size()));
	}
	
	public String toString() {
		return Formatter.format("%s %d/%dhp", name, hp, maxHp);
	}
}
