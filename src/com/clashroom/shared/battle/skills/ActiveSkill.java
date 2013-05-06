package com.clashroom.shared.battle.skills;

import java.util.Random;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.actions.ActionSkill;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;

public abstract class ActiveSkill extends Skill {
	private static final long serialVersionUID = 1L;

	public static int DAMAGE_FACTOR_MULT = 5;
	public static float ACCURACY_PERFECT = -1;

	public enum Target {
		Self, One, Splash, All
	}
	
	protected Target target;
	protected boolean targetAllies;
	protected int baseDamage;
	/** [0+] */
	protected double damageFactor;
	/** [0+] */
	protected double accuracyFactor;
	/** [0+] */
	protected double rangeFactor;
	protected int mpCost;
	
	public Target getTarget() {
		return target;
	}

	public boolean targetsAllies() {
		return targetAllies;
	}

	public int getMpCost() {
		return mpCost;
	}

	protected ActiveSkill(String name, String icon, Attribute attribute, 
			Target target, boolean targetAllies, int baseDamage,
			double damageFactor, double accuracyFactor, double rangeFactor,
			int mpCost) {
		super(name, icon, attribute);

		this.target = target;
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
//			double chance = accuracyFactor * Math.sqrt(
//					attacker.agility / (double)target.agility);
//			return chance < random.nextDouble() * 1.5;
			
			double missChance = (10 - (attacker.agility - target.agility)) / 100.0;
			return missChance > random.nextDouble();
		}
	}
	
	
	public String getAttackString(Battler attacker, Battler target) {
		String targetName = target == attacker ? "itself" : target.name;
		return Formatter.format("%s cast %s on %s", attacker.name, name, targetName);
	}
	
	public String getAttackString(Battler attacker) {
		return Formatter.format("%s cast %s", attacker.name, name);
	}
}
