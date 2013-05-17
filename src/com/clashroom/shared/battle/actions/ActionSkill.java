package com.clashroom.shared.battle.actions;

import java.util.LinkedList;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.buffs.Buff;
import com.clashroom.shared.battle.skills.ActiveSkill;

/**
 * A {@link BattleAction} occurring when a {@link Battler} uses a skill
 * on another Battler. This class is used for skill that target one Battler
 * or deals splash damage, but {@link ActionSkillTargetAll} is used if the
 * skill targets all enemies or allies, as the mechanic works differently in
 * this case.
 */
public class ActionSkill extends BattleAction {
	private static final long serialVersionUID = 1L;
	
	public Battler attacker;
	public ActiveSkill skill;
	//damages are a list because splash damage will have multiple damages
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
			//If we missed, that's the end of that
			damageString = " and missed!";
		} else {
			damageString = "";
			if (skill.getBaseDamage() != 0) {
				damageString = skill.targetsAllies() ? " healing " : " dealing ";
				for (int i = 0; i < damages.size(); i++) {
					if (i > 0) damageString += i == damages.size() - 1 ? " and " : ", "; //add commas/ands
					Damage damage = damages.get(i);
					damageString += Formatter.format("%d damage", Math.abs(damage.damage));
					if (damages.size() > 1) {
						//Only need to specify whom you're attacking if it's more than one enemy
						damageString += Formatter.format(" to %s", damage.target.name);
					}
				}
			}
			damageString += "!";
			if (critical) damageString += " A critical hit!";
			//Inform of buffs dealt
			for (int i = 0; i < damages.size(); i++) {
				Damage damage = damages.get(i);
				if (damage.buff != null) {
					damageString += Formatter.format(" %s was %s!", 
							damage.target.name, damage.buff.getName());
				}
			}
		}
		return attackString + damageString;
	}
	
	/**
	 * Represents damage to one target. An attack can have multiple damages.
	 */
	public static class Damage {
		public Battler target;
		public int damage;
		public Buff buff;
		
		public Damage(Battler target, int damage, Buff buff) {
			this.target = target;
			this.damage = damage;
			this.buff = buff;
		}
	}
}
