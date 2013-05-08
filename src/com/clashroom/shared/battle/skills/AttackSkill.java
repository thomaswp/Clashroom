package com.clashroom.shared.battle.skills;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.battlers.Battler;

public class AttackSkill extends ActiveSkill {
	private static final long serialVersionUID = 1L;
	
	public AttackSkill() {
		super("Attack", null, Attribute.Strength, Target.One, false, 0, 1,
				1, 0.1, 0);
	}

	@Override
	protected double getAttackModifier(Battler attacker) {
		return attacker.getMeleeModifier();
	}
	
	@Override
	public String getAttackString(Battler attacker, Battler target) {
		return Formatter.format("%s attacked %s", attacker.name, target.name);
	}
	
	@Override
	public boolean showSkill() {
		return false;
	}

	@Override
	public String getDescription() {
		return "";
	}

	@Override
	public int getSkillPointCost() {
		return 0;
	}
}
