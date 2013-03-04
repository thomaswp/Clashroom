package com.clashroom.shared.actions;

import java.util.LinkedList;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.actions.ActionSkill.Damage;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.skills.Skill;

public class ActionSkillTargetAll extends BattleAction {
	public Battler attacker;
	public Skill skill;
	public LinkedList<ActionSkill> attacks;


	public ActionSkillTargetAll(Battler attacker, Skill skill, LinkedList<ActionSkill> attacks) {
		super();
		this.attacker = attacker;
		this.skill = skill;
		this.attacks = attacks;
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
				damageString += skill.targetAllies ? "healing " : "dealing ";
				damageString += Formatter.format("%d damage", Math.abs(damage.damage));
				damageString += Formatter.format(" to %s", damage.target.name);
			}
			damageString += "!";
		}
		return attackString + " " + damageString;
	}
}
