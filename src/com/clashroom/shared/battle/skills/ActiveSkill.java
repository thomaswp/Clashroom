package com.clashroom.shared.battle.skills;

import java.util.Random;

import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.actions.ActionSkill;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.buff.Buff;

public abstract class ActiveSkill extends Skill {
	private static final long serialVersionUID = 1L;

	public static final double CRITICAL_MULT = 2;
	public static float ACCURACY_PERFECT = -1;

	public enum Target {
		Self, One, Splash, All
	}
	
	protected Target target;
	protected boolean targetAllies;
	protected int baseDamage;
	/** [0+] */
	//protected double damageFactor;
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
		//this.damageFactor = damageFactor;
		this.accuracyFactor = accuracyFactor;
		this.rangeFactor = rangeFactor;
		this.mpCost = mpCost;
	}
	
	protected double getAttackModifier(Battler attacker) {
		return attacker.getSpellModifier();
	}

	public Damage getDamage(Battler attacker, Battler target, Random random) {
		double dmg = baseDamage * getAttackModifier(attacker);
		dmg += dmg * rangeFactor * (random.nextDouble() - 0.5) * 2;
		int damage = (int)Math.max(dmg, 0);
		if (targetAllies) damage *= -1;
		return new Damage(target, damage, getBuff());
	}
	
	public ActionSkill getAttack(Battler attacker, Battler target, Random random) {
		boolean critical = getCritical(attacker, random);
		return getAttack(attacker, target, critical, random);
	}

	public ActionSkill getAttack(Battler attacker, Battler target, boolean critical,
			Random random) {
		boolean miss = getMiss(attacker, target, random);
		critical &= !miss;
		
		Damage damage = getDamage(attacker, target, random);
		if (miss) {
			damage.damage = 0;
			damage.buff = null;
		}
		
		if (critical) damage.damage *= CRITICAL_MULT;
		
		return new ActionSkill(attacker, this, miss, critical, damage);
	}
	
	public boolean getCritical(Battler attacker, Random random) {
		if (targetAllies) return false;
		return attacker.getCriticalChance() > random.nextDouble();
	}
	
	public boolean getMiss(Battler attacker, Battler target, Random random) {
		if (targetAllies || accuracyFactor == ACCURACY_PERFECT) {
			return false;
		} else {
			double missChance = target.getDodgeChance();
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
	
	public Buff getBuff() {
		return null;
	}
}
