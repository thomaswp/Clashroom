package com.clashroom.shared.battle.actions;

import java.io.Serializable;

import com.clashroom.client.battle.BattlePage;

/**
 * Represents one action that can occur in a battle, which will be animated
 * or displayed when going through a battle on the {@link BattlePage}.
 */
@SuppressWarnings("serial")
public abstract class BattleAction implements Serializable {
	/**
	 * A String representation of the action
	 */
	public abstract String toBattleString();
}
