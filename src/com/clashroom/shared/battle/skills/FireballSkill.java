package com.clashroom.shared.battle.skills;

public class FireballSkill extends ActiveSkill {
	private static final long serialVersionUID = 1L;

	public FireballSkill() {
		super("Fireball", "fireball-red-1.png", Attribute.Intelligence, Target.One, false, 140, 
				0.8, 0.4, 50);
	}

	@Override
	public String getDescription() {
		return "Launches a fireball at a single opponent.";
	}

	@Override
	public int getSkillPointCost() {
		return 1;
	}

}
