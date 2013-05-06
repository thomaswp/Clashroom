package com.clashroom.shared.battle.actions;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.battlers.Battler;

public class ActionExp extends BattleAction {
	private static final long serialVersionUID = 1L;
	
	public Battler battler;
	public int exp;
	private int oldLevel;
	public int newLevel;
	
	@Deprecated
	public ActionExp() { }
	
	public ActionExp(Battler battler, int exp, int newLevel) {
		this.battler = battler;
		this.exp = exp;
		this.newLevel = newLevel;
		oldLevel = battler.level;
	}

	@Override
	public String toBattleString() {
		String expGain = Formatter.format("%s gained %d experience!", battler.name, exp);
		if (oldLevel != battler.level) {
			expGain += Formatter.format(" %s grew to Level %d!", battler.name, newLevel);
		}
		return expGain;
	}
}
