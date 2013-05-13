package com.clashroom.shared.battle.skills;

public class StormSkill extends ActiveSkill {

	private static final long serialVersionUID = 1L;

	public StormSkill() {
		super("Storm", "air-burst-sky-1.png", Attribute.Intelligence, Target.Splash, false, 115,
				0.8f, 0.15f, 75);
	}

	@Override
	public int getSkillPointCost() {
		return 3;
	}

	@Override
	public String getDescription() {
		return "Strikes an opponent with roaring winds, damaging nearby foes as well.";
	}

}
