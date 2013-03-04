package com.clashroom.shared.skills;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battlers.Battler;

public class AttackSkill extends Skill {
	public AttackSkill() {
		super("Attack", Target.One, Attribute.Strength, false, 0, 1,
				1, 0.1, 0);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String getAttackString(Battler attacker, Battler target) {
		return Formatter.format("%s attacked %s", attacker.name, target.name);
	}
}
