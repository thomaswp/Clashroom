package com.clashroom.shared.battle.actions;

import com.clashroom.shared.Formatter;

public class ActionFinish extends BattleAction {
	private static final long serialVersionUID = 1L;
	
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
