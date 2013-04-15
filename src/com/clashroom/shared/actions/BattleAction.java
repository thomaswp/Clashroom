package com.clashroom.shared.actions;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class BattleAction implements Serializable {
	public abstract String toBattleString();
}
