package com.clashroom.shared.skills;

public class HealSkill extends Skill {
	private static final long serialVersionUID = 1L;

	public HealSkill() {
		super("Heal", Target.All, Attribute.Intelligence, true, 50, 1,
				ACCURACY_PERFECT, 0.25, 50);
	}

}
