package com.clashroom.shared.battle.skills;

import com.clashroom.shared.Constant;
import com.clashroom.shared.battle.buffs.BlessedBuff;
import com.clashroom.shared.battle.buffs.Buff;

public class BlessingSkill extends ActiveSkill {
	private static final long serialVersionUID = 1L;

	public BlessingSkill() {
		super("Blessing", "runes-blue-1.png", Attribute.Intelligence, Target.One, true, 0, 
				ACCURACY_PERFECT, 0, 50);
	}

	@Override
	public int getSkillPointCost() {
		return 5;
	}

	@Override
	public String getDescription() {
		return "Grants itself or an ally a blessing, giving extra " + Constant.STAT_STR + " for the battle.";
	}
	
	@Override
	public Buff getBuff() {
		return new BlessedBuff(1.5f);
	}

}
