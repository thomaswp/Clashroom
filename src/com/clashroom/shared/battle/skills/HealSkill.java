package com.clashroom.shared.battle.skills;

public class HealSkill extends ActiveSkill {
	private static final long serialVersionUID = 1L;

	public HealSkill() {
		super("Heal", "heal-jade-1.png", Attribute.Intelligence, Target.One, true, 50, 1,
				ACCURACY_PERFECT, 0.25, 50);
	}

	@Override
	public String getDescription() {
		return "Heals a single ally some of their health back.";
	}

	@Override
	public int getSkillPointCost() {
		// TODO Auto-generated method stub
		return 3;
	}
}
