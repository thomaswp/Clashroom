package com.clashroom.shared.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.skills.AttackSkill;
import com.clashroom.shared.skills.FireBreathSkill;
import com.clashroom.shared.skills.FireballSkill;
import com.clashroom.shared.skills.HealSkill;

public class GoblinBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	public GoblinBattler(int level) {
		name = "Goblin";
		image = "goblin.png";
		this.level = level;
		strength = getStatCurve(level, 3, 5);
		agility = getStatCurve(level, 2, 4);
		intelligence = getStatCurve(level, 1, 2);
		description = getDescription();
		generateMaxHP();
		generateMaxMP();
	}
	
	@Deprecated
	public GoblinBattler() {}

	@Override
	protected String getDescription() {
		return Formatter.format("Level %d %s", level, name);
	}
	
}
