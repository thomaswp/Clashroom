package com.clashroom.shared.battle.actions;

import java.util.LinkedList;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.skills.ActiveSkill;

/**
 * A variant of {@link ActionSkill} for attacks that target all enemies/allies.
 */
public class ActionSkillTargetAll extends BattleAction {
	private static final long serialVersionUID = 1L;
	
	public Battler attacker;
	public ActiveSkill skill;
	public LinkedList<ActionSkill> attacks;
	//all damages share the same critical, but might hit/miss separately
	public boolean critical;

	/**
	 * Composes an {@link ActionSkillTargetAll} from a group of {@link ActionSkill}s. 
	 * @param attacker The attacker
	 * @param skill The skill used
	 * @param critical If the attack was a critical
	 * @param attacks The attacks to combine - these are assumed to have the same attacker/skill as above
	 */
	public ActionSkillTargetAll(Battler attacker, ActiveSkill skill, boolean critical, LinkedList<ActionSkill> attacks) {
		super();
		this.attacker = attacker;
		this.skill = skill;
		this.attacks = attacks;
		this.critical = critical;
	}

	@Override
	public String toBattleString() {
		String attackString = skill.getAttackString(attacker);

		String damageString = "";
		for (int i = 0; i < attacks.size(); i++) {
			ActionSkill attack = attacks.get(i);
			Damage damage = attack.getPrimaryDamage();
			if (i > 0) damageString += i == attacks.size() - 1 ? " and " : ", "; //add commas/ands
			if (attack.missed) {
				damageString += "missing " + damage.target.name;
			} else {
				damageString += skill.targetsAllies() ? "healing " : "dealing ";
				damageString += Formatter.format("%d damage", Math.abs(damage.damage));
				damageString += Formatter.format(" to %s", damage.target.name);
			}
		}
		damageString += "!";
		if (critical) damageString += " A critical hit!";
		for (int i = 0; i < attacks.size(); i++) {
			ActionSkill attack = attacks.get(i);
			Damage damage = attack.getPrimaryDamage();
			if (damage.buff != null) {
				damageString += Formatter.format(" %s was %s!", 
						damage.target.name, damage.buff.getName());
			}
		}
		return attackString + " " + damageString;
	}
}
