package com.clashroom.shared.battle.skills;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.buff.Buff;

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
	
	protected abstract void addBuffs(List<Buff> buffs);
}
