package com.clashroom.shared.battle.skills;

public class LightningSkill extends ActiveSkill {
	private static final long serialVersionUID = 1L;

	public LightningSkill() {
		super("Lightning", "lighting-eerie-1.png", Attribute.Intelligence, Target.One, 
				false, 125, 1, 0.2, 60);
	}

	@Override
	public int getSkillPointCost() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "Strikes a single opponent with lightning";
	}

}
