package com.clashroom.shared.battle.skills;


public class FireBreathSkill extends ActiveSkill {
	private static final long serialVersionUID = 1L;

	public FireBreathSkill() {
		super("Fire Breath", "beam-red-1.png", Attribute.Intelligence, Target.All, false, 140, 
				0.75, 0.2, 75);
	}

	@Override
	public String getDescription() {
		return "Breathes scorching fire over all opponents.";
	}

	@Override
	public int getSkillPointCost() {
		return 5;
	}
}
