package com.clashroom.shared.battle.skills;


public class FireBreathSkill extends Skill {
	private static final long serialVersionUID = 1L;

	public FireBreathSkill() {
		super("Fire Breath", Target.All, Attribute.Intelligence, false, 25, 1,
				0.7, 0.2, 75);
	}

}
