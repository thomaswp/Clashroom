package com.clashroom.shared.actions;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battlers.Battler;

public class ActionDeath extends BattleAction {
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
