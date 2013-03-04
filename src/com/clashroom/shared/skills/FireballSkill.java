package com.clashroom.shared.skills;

public class FireballSkill extends Skill {
	private static final long serialVersionUID = 1L;

	public FireballSkill() {
		super("Fireball", Target.Splash, Attribute.Intelligence, false, 100, 1,
				0.7, 0.5, 50);
	}

}
