package com.clashroom.shared.battle.skills;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.buffs.Buff;

/**
 * Represents a passive {@link Skill}, which a {@link Battler} does not
 * actively use on their turn, but which affects its stats.
 */
public abstract class PassiveSkill extends Skill {
	private static final long serialVersionUID = 1L;

	private transient List<Buff> buffs = new ArrayList<Buff>();

	public List<Buff> getBuffs() {
		if (buffs == null) {
			buffs = new ArrayList<Buff>();
			addBuffs(buffs);
		}
		return buffs;
	}
	
	protected PassiveSkill(String name, String icon, Attribute attribute) {
		super(name, icon, attribute);
		addBuffs(buffs);
	}
	
	/**
	 * Should add any {@link Buff}s given by this Skill to the provided list.
	 * @param buffs The list to which to add any Buffs.
	 */
	protected abstract void addBuffs(List<Buff> buffs);
}
