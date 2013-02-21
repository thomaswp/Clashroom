package com.clashroom.shared;

public class ActionDeath extends BattleAction {
	public Battler battler;
	
	public ActionDeath(Battler battler) {
		super();
		this.battler = battler;
	}

	@Override
	public String toBattleString() {
		return String.format("%s fainted!", battler.name);
	}
}
