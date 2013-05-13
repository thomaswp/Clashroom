package com.clashroom.shared.battle.actions;

import java.util.LinkedList;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.skills.ActiveSkill;

public class ActionSkillTargetAll extends BattleAction {
	private static final long serialVersionUID = 1L;
	
	public Battler attacker;
	public ActiveSkill skill;
	public LinkedList<ActionSkill> attacks;
	public boolean critical;


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
			if (i > 0) damageString += i == attacks.size() - 1 ? " and " : ", ";
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
						damage.target.name, damage.buff);
			}
		}
		return attackString + " " + damageString;
	}
}
