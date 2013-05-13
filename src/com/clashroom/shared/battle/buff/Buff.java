package com.clashroom.shared.battle.buff;

import java.util.HashMap;

import com.clashroom.shared.Debug;

public abstract class Buff {
	public abstract String getName();
	public abstract String getDescription();
	public abstract String getIcon();
	public abstract void setModifiers();
	
	public enum Stat {
		MaxHp, MaxMp, Str, Agi, Int, Crit, Dodge, Melee, Spell
	}
	
	private HashMap<Stat, Modifier> modifierMap = 
			new HashMap<Buff.Stat, Buff.Modifier>();
	
	public Buff() {
		for (Stat stat : Stat.values()) {
			modifierMap.put(stat, new Modifier());
		}
		setModifiers();
	}
	
	protected void setModifier(Stat stat, int plus, float factor) {
		Modifier mod = modifierMap.get(stat);
		mod.plus = plus;
		mod.factor = factor;
	}
	
	public Modifier getModifier(Stat stat) {
		return modifierMap.get(stat);
	}
	
	public static class Modifier {
		protected int plus = 0;
		protected float factor = 1;
		
		public float getFactor() {
			return factor;
		}
		
		public int getPlus() {
			return plus;
		}
	}
}
