package com.clashroom.shared.battle.buff;

import com.clashroom.shared.Constant;

public class BlessedBuff extends Buff {

	private float strength;
	
	public BlessedBuff(float strength) {
		this.strength = strength;
		setModifiers();
	}
	
	@Override
	public String getName() {
		return "Blessed";
	}

	@Override
	public String getDescription() {
		return "This Dragon is blessed and has extra " + Constant.STAT_STR + ".";
	}

	@Override
	public void setModifiers() {
		setModifier(Stat.Str, 0, strength);
	}

	@Override
	public String getIcon() {
		return "runes-orange-1.png";
	}

}
