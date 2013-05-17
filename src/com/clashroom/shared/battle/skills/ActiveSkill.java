package com.clashroom.shared.battle.skills;

import java.util.Random;

import com.clashroom.shared.Constant;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.actions.ActionSkill;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.buffs.Buff;

/**
 * Represents an active {@link Skill}, which a {@link Battler} uses
 * to deal/heal damage/buffs on their turn.
 */
public abstract class ActiveSkill extends Skill {
	private static final long serialVersionUID = 1L;

	/** The increase in damage for critical hits */
	public static final double CRITICAL_MULT = 1.7;
	/** Denotes a Skill has perfect accuracy */ 
	public static float ACCURACY_PERFECT = -1;

	/**
	 * Defines how many targets a skill has.
	 */
	public enum Target {
		/** Targets one's self */
		Self,
		/** Targets one enemy/ally */
		One, 
		/** Targets one enemy/ally but also hits nearby battlers */
		Splash, 
		/** Targets all enemies/allies */
		All
	}
	
	protected Target target;
	protected boolean targetAllies;
	/**
	 * The base damage to be multiplied by the battler's spell or melee factor.
	 */
	protected int baseDamage;
	/** 
	 * The chance of hitting with this skill is multiplies by this factor: 
	 * [0,1], or ACCURACY_PERFECT for perfect accuracy. 
	 */
	protected double accuracyFactor;
	/** 
	 * The portion of this skill's damage which is up to change: [0,1].
	 * Setting this to 0 means the skill deals consistent damage, and
	 * setting it to 1 would indicate it could do 0-200% of its base damage.
	 */
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

	/**
	 * Returns the base damage of this Skill, which will be modified
	 * by a spell of melee modifier. If this value is 0, the Spell is
	 * not intended to damage, but likely inflicts a {@link Buff}.
	 */
	public int getBaseDamage() {
		return baseDamage;
	}

	/**
	 * Creates an {@link ActionSkill} with the following properties.
	 * 
	 * @param name The name of the Skill
	 * @param icon The name of the icon file to represent this Skill, relative to the
	 * {@link Constant#IMG_ICON} directory
	 * @param attribute The {@link Attribute} with which to associate this skill - currently does nothing
	 * @param target How many enemies/allies this Skill targets
	 * @param targetAllies Whether this Skill targets allies or enemies
	 * @param baseDamage Base damage of the Skill, before modification. See {@link AttackSkill}'s value for a good
	 * baseline. This value can be negative to denote healing.
	 * @param accuracyFactor The {@link ActiveSkill#accuracyFactor} for this Skill
	 * @param rangeFactor The {@link ActiveSkill#rangeFactor} for this Skill
	 * @param mpCost The mp cost of casting this Skill
	 */
	protected ActiveSkill(String name, String icon, Attribute attribute, 
			Target target, boolean targetAllies, int baseDamage,
			double accuracyFactor, double rangeFactor,
			int mpCost) {
		super(name, icon, attribute);

		this.target = target;
		this.targetAllies = targetAllies;
		this.baseDamage = baseDamage;
		this.accuracyFactor = accuracyFactor;
		this.rangeFactor = rangeFactor;
		this.mpCost = mpCost;
	}
	
	/**
	 * Use the method you want to use to generate the modifier by which
	 * the base damage is multiplied. By default, this returns
	 * {@link Battler#getSpellModifier()}, but it can be overwritten
	 * to use {@link Battler#getMeleeModifier()} instead, or another special case.
	 */
	protected double getAttackModifier(Battler attacker) {
		return attacker.getSpellModifier();
	}

	/**
	 * Gets a {@link Damage} for this Skill, the given attacker and target.
	 * @param attacker The attacking Battler
	 * @param target The defending Batler
	 * @param random The seeded Random to use for calculations 
	 * @return The Damage representing the attack
	 */
	public Damage getDamage(Battler attacker, Battler target, Random random) {
		double dmg = baseDamage * getAttackModifier(attacker);
		dmg += dmg * rangeFactor * (random.nextDouble() - 0.5) * 2;
		int damage = (int)Math.max(dmg, 0);
		if (targetAllies) damage *= -1;
		//TODO: Make the buff below have a chance of infliction, rather than just
		//always happening
		return new Damage(target, damage, getBuff());
	}
	
	/**
	 * Gets an {@link ActionSkill} representing an attack using this Skill with the
	 * given attacker and target.
	 * @param attacker The attacking Battler
	 * @param target The defending Battler
	 * @param random The seeded Random to use for calculations
	 * @return The ActionSkill
	 */
	public ActionSkill getAttack(Battler attacker, Battler target, Random random) {
		boolean critical = getCritical(attacker, random);
		return getAttack(attacker, target, critical, random);
	}

	/**
	 * Gets an {@link ActionSkill} representing an attack using this Skill with the
	 * given attacker and target. Forces the attack to have the given critical.
	 * @param attacker The attacking Battler
	 * @param target The defending Battler
	 * @param critical Whether or not this attack should be critical
	 * @param random The seeded Random to use for calculations
	 * @return The ActionSkill
	 */
	public ActionSkill getAttack(Battler attacker, Battler target, boolean critical,
			Random random) {
		boolean miss = getMiss(attacker, target, random);
		critical &= !miss;
		critical &= baseDamage > 0;
		
		Damage damage = getDamage(attacker, target, random);
		if (miss) {
			damage.damage = 0;
			damage.buff = null;
		}
		
		if (critical) damage.damage *= CRITICAL_MULT;
		
		return new ActionSkill(attacker, this, miss, critical, damage);
	}
	
	/**
	 * Randomly determines if an attack is critical, based off the
	 * attacker's {@link Battler#getCriticalChance()}.
	 * @param attacker The attacking Battler
	 * @param random The seeded Random to be used for calculations
	 * @return true if the attack is critical
	 */
	public boolean getCritical(Battler attacker, Random random) {
		if (targetAllies) return false;
		return attacker.getCriticalChance() > random.nextDouble();
	}
	
	/**
	 * Randomly determines if an attack missed, based off the
	 * defender's {@link Battler#getDodgeChance()}.
	 * @param attacker The attacking Battler
	 * @param target The defending Battler
	 * @param random The seeded Random to be used for calculations
	 * @return true if the attack misses
	 */
	public boolean getMiss(Battler attacker, Battler target, Random random) {
		if (targetAllies || accuracyFactor == ACCURACY_PERFECT) {
			return false;
		} else {
			double missChance = target.getDodgeChance() / accuracyFactor;
			return missChance > random.nextDouble();
		}
	}
	
	/**
	 * A String of the form "attacker attacked target" representing
	 * an use of this Skill on another {@link Battler}.
	 * @param attacker The attacking Battler
	 * @param target The defending Battler
	 * @return The String representing the spell use
	 */
	public String getAttackString(Battler attacker, Battler target) {
		String targetName = target == attacker ? "itself" : target.name;
		return Formatter.format("%s cast %s on %s", attacker.name, name, targetName);
	}
	
	/**
	 * A String of the form "attacker attacked" representing
	 * an use of this Skill without the target being known
	 * @param attacker The attacking Battler
	 * @return The String representing the spell use
	 */
	public String getAttackString(Battler attacker) {
		return Formatter.format("%s cast %s", attacker.name, name);
	}
	
	/**
	 * Returns a {@link Buff} which this attack inflicts, or
	 * null if none is inflicted.
	 */
	public Buff getBuff() {
		return null;
	}
}
