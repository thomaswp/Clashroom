package com.clashroom.shared.actions;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battlers.Battler;

public class ActionFinish extends BattleAction {
	public String victorName;
	public boolean teamAVictor;

	public ActionFinish(boolean teamAVictor, String victorName) {
		super();
		this.teamAVictor = teamAVictor;
		this.victorName = victorName;
	}

	@Override
	public String toBattleString() {
		return Formatter.format("%s was the winner!", victorName);
	}
}
