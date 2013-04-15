package com.clashroom.shared.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.skills.FireballSkill;
import com.clashroom.shared.skills.HealSkill;

public class FairyBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	public FairyBattler() { }
	
	public FairyBattler(int level) {
		name = "Fairy";
		image = "fairy.png";
		this.level = level;
		strength = getStatCurve(level, 1, 2);
		agility = getStatCurve(level, 2, 4);
		intelligence = getStatCurve(level, 3, 5);
		description = getDescription();
		skills.add(new FireballSkill());
		skills.add(new HealSkill());
		generateMaxHP();
		generateMaxMP();
	}

	@Override
	protected String getDescription() {
		return Formatter.format("Level %d %s", level, name);
	}

}
