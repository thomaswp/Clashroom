package com.clashroom.shared.battle.skills;

import com.clashroom.shared.Constant;

public class HealSkill extends ActiveSkill {
	private static final long serialVersionUID = 1L;

	public HealSkill() {
		super("Heal", "heal-royal-1.png", Attribute.Intelligence, Target.One, true, 75, 
				ACCURACY_PERFECT, 0.25, 50);
	}

	@Override
	public String getDescription() {
		return "Heals this dragon or an ally to restore some " + Constant.STAT_HP + ".";
	}

	@Override
	public int getSkillPointCost() {
		return 3;
	}
}
