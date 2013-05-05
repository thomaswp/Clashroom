package com.clashroom.shared.battle.skills;

import com.clashroom.shared.Constant;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battle.battlers.Battler;

public class EggShellSkill extends PassiveSkill {//permanent   
	private static final long serialVersionUID = 1L;

	public EggShellSkill() {
		super("Tough Egg Shell", "protect-blue-1.png", Attribute.None);
	}

	@Override
	public int getSkillPointCost() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "A tough egg shell protects you, giving you a permanent bonus to " + Constant.STAT_HP;
	}

	@Override
	public void applyBuff(Battler battler) {
		int plus = Math.round(battler.maxHp * 0.1f);
		Debug.write("%d, %d", battler.maxHp, plus);
		battler.maxHp += plus;
	}

}
