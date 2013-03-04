package com.clashroom.shared.actions;

import java.util.LinkedList;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.skills.Skill;

public class ActionSkill extends BattleAction {
	public Battler attacker;
	public Skill skill;
	public LinkedList<Damage> damages = new LinkedList<ActionSkill.Damage>();
	public boolean missed;
	
	public Damage getPrimaryDamage() {
		return damages.get(0);
	}
	
	public ActionSkill(Battler attacker, Skill skill, boolean missed, Damage damage) {
		super();
		this.attacker = attacker;
		this.skill = skill;
		this.missed = missed;
		damages.add(damage);
	}

	@Override
	public String toBattleString() {
		String attackString = skill.getAttackString(attacker, getPrimaryDamage().target);
		String damageString;
		if (missed) {
			damageString = "and missed!";
		} else {
			damageString = skill.targetAllies ? "healing " : "dealing ";
			for (int i = 0; i < damages.size(); i++) {
				if (i > 0) damageString += i == damages.size() - 1 ? " and " : ", ";
				Damage damage = damages.get(i);
				damageString += Formatter.format("%d damage", Math.abs(damage.damage));
				if (damages.size() > 1) {
					damageString += Formatter.format(" to %s", damage.target.name);
				}
			}
			damageString += "!";
		}
		return attackString + " " + damageString;
	}
	
	public static class Damage {
		public Battler target;
		public int damage;
		
		public Damage(Battler target, int damage) {
			this.target = target;
			this.damage = damage;
		}
	}
}
