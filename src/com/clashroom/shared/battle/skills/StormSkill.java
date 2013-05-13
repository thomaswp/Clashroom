package com.clashroom.shared.battle.skills;

public class StormSkill extends ActiveSkill {

	private static final long serialVersionUID = 1L;

	public StormSkill() {
		super("Storm", "air-burst-sky-1.png", Attribute.Intelligence, Target.Splash, false, 50, 1,
				0.8f, 0.5f, 100);
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
