package com.clashroom.shared.battle.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.Battle;

/**
 * An NPC {@link Battler} for testing, an hopefully for
 * in-game NPC {@link Battle}s in the future.
 */
public class GoblinBattler extends Battler {
	private static final long serialVersionUID = 1L;

	//Empty constructor is necessary for passing over RPC
	@Deprecated
	public GoblinBattler() {}
	
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

	@Override
	protected String getDescription() {
		return Formatter.format("Level %d %s", level, name);
	}

	@Override
	protected Battler copyChild() {
		return new GoblinBattler();
	}
	
}
