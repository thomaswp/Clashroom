package com.clashroom.shared.battle.actions;

import java.util.LinkedList;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.skills.ActiveSkill;

public class ActionSkill extends BattleAction {
	private static final long serialVersionUID = 1L;
	
	public Battler attacker;
	public ActiveSkill skill;
	public LinkedList<Damage> damages = new LinkedList<ActionSkill.Damage>();
	public boolean missed;
	public boolean critical;
	
	public Damage getPrimaryDamage() {
		return damages.get(0);
	}
	
	public ActionSkill(Battler attacker, ActiveSkill skill, boolean missed, boolean critical, Damage damage) {
		super();
		this.attacker = attacker;
		this.skill = skill;
		this.missed = missed;
		this.critical = critical;
		damages.add(damage);
	}

	@Override
	public String toBattleString() {
		String attackString = skill.getAttackString(attacker, getPrimaryDamage().target);
		String damageString;
		if (missed) {
			damageString = "and missed!";
		} else {
			damageString = skill.targetsAllies() ? "healing " : "dealing ";
			for (int i = 0; i < damages.size(); i++) {
				if (i > 0) damageString += i == damages.size() - 1 ? " and " : ", ";
				Damage damage = damages.get(i);
				damageString += Formatter.format("%d damage", Math.abs(damage.damage));
				if (damages.size() > 1) {
					damageString += Formatter.format(" to %s", damage.target.name);
				}
			}
			damageString += "!";
			if (critical) damageString += " A critical hit!";
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
