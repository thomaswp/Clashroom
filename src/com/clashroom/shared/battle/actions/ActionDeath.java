package com.clashroom.shared.battle.actions;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.battlers.Battler;

public class ActionDeath extends BattleAction {
	private static final long serialVersionUID = 1L;
	
	public Battler battler;
	
	public ActionDeath(Battler battler) {
		super();
		this.battler = battler;
	}

	@Override
	public String toBattleString() {
		return Formatter.format("%s fainted!", battler.name);
	}
}
