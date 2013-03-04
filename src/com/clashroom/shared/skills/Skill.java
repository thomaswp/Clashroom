package com.clashroom.shared.skills;

import java.io.Serializable;
import java.util.Random;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.actions.ActionSkill;
import com.clashroom.shared.actions.ActionSkill.Damage;
import com.clashroom.shared.actions.BattleAction;
import com.clashroom.shared.battlers.Battler;

public abstract class Skill implements Serializable {
	private static final long serialVersionUID = 1L;

	public static int DAMAGE_FACTOR_MULT = 5;
	public static float ACCURACY_PERFECT = -1;
	
	public enum Target {
		Self, One, Splash, All
	}
	
	public enum Attribute {
		Strength, Agility, Intelligence
	}
	
	public String name;
	public Target target;
	public Attribute attribute;
	public boolean targetAllies;
	public int baseDamage;
	/** [0+] */
	public double damageFactor;
	/** [0+] */
	public double accuracyFactor;
	/** [0+] */
	public double rangeFactor;
	public int mpCost;
	
	public Skill(String name, Target target, Attribute attribute, boolean targetAllies,
			int baseDamage, double damageFactor, double accuracyFactor,
			double rangeFactor, int mpCost) {
		super();
		this.name = name;
		this.target = target;
		this.attribute = attribute;
		this.targetAllies = targetAllies;
		this.baseDamage = baseDamage;
		this.damageFactor = damageFactor;
		this.accuracyFactor = accuracyFactor;
		this.rangeFactor = rangeFactor;
		this.mpCost = mpCost;
	}

	public Damage getDamage(Battler attacker, Battler target, Random random) {
		int attackerStr = getAttribute(attacker, attribute);
		//int targetStr = getAttribute(attacker, attribute);
		double dmg = baseDamage + attackerStr * damageFactor * DAMAGE_FACTOR_MULT;
		dmg += dmg * rangeFactor * (random.nextDouble() - 0.5) * 2;
		int damage = (int)Math.max(dmg, 0);
		if (targetAllies) damage *= -1;
		return new Damage(target, damage);
	}
	
	public ActionSkill getAttack(Battler attacker, Battler target, Random random) {
		boolean miss = getMiss(attacker, target, random);
		
		Damage damage = getDamage(attacker, target, random);
		if (miss) damage.damage = 0;
		
		return new ActionSkill(attacker, this, miss, damage);
	}
	
	public boolean getMiss(Battler attacker, Battler target, Random random) {
		if (targetAllies || accuracyFactor == ACCURACY_PERFECT) {
			return false;
		} else {
			double chance = accuracyFactor * Math.sqrt(
					attacker.agility / (double)target.agility);
			return chance < random.nextDouble() * 1.5;
		}
	}
	
	private static int getAttribute(Battler battler, Attribute attribute) {
		switch (attribute) {
		case Strength: return battler.strength;
		case Agility: return battler.agility;
		case Intelligence: return battler.intelligence;
		}
		return 0;
	}
	
	public String getAttackString(Battler attacker, Battler target) {
		String targetName = target == attacker ? "itself" : target.name;
		return Formatter.format("%s cast %s on %s", attacker.name, name, targetName);
	}
	
	public String getAttackString(Battler attacker) {
		return Formatter.format("%s cast %s", attacker.name, name);
	}
}
