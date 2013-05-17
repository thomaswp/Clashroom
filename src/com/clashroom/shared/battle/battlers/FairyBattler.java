package com.clashroom.shared.battle.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.Battle;
import com.clashroom.shared.battle.skills.FireballSkill;
import com.clashroom.shared.battle.skills.HealSkill;

/**
 * An NPC {@link Battler} for testing, an hopefully for
 * in-game NPC {@link Battle}s in the future.
 */
public class FairyBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	//Empty constructor is necessary for passing over RPC
	@Deprecated
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

	@Override
	protected Battler copyChild() {
		return new FairyBattler();
	}
}
