package com.clashroom.shared.actions;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battlers.Battler;

public class ActionFinish extends BattleAction {
	public String victor;

	public ActionFinish(String victor) {
		super();
		this.victor = victor;
	}

	@Override
	public String toBattleString() {
		return Formatter.format("%s was the winner!", victor);
	}
}
