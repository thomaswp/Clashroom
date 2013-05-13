package com.clashroom.shared.battle.buff;

import com.clashroom.shared.Constant;

public class BlessedBuff extends Buff {

	private int strength;
	
	public BlessedBuff(int strength) {
		this.strength = strength;
	}
	
	@Override
	public String getName() {
		return "Blessed";
	}

	@Override
	public String getDescription() {
		return "This Dragon is blessed and has extra " + Constant.STAT_STR;
	}

	@Override
	public void setModifiers() {
		setModifier(Stat.Str, strength, 0);
		
	}

}
