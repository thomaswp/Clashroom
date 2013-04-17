package com.clashroom.shared.battle.actions;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class BattleAction implements Serializable {
	public abstract String toBattleString();
}
