package com.clashroom.shared;

public class ActionFinish extends BattleAction {
	public Battler victor;

	public ActionFinish(Battler victor) {
		super();
		this.victor = victor;
	}

	@Override
	public String toBattleString() {
		return Formatter.format("%s was the winner!", victor.name);
	}
}
