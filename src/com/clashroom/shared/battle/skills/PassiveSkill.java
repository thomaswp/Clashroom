package com.clashroom.shared.battle.skills;

import com.clashroom.shared.battle.battlers.Battler;

public abstract class PassiveSkill extends Skill {
	private static final long serialVersionUID = 1L;

	protected PassiveSkill(String name, String icon, Attribute attribute) {
		super(name, icon, attribute);
	}
	
	public abstract void applyBuff(Battler battler);
}
