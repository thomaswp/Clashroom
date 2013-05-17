package com.clashroom.shared.battle.buffs;

import java.util.HashMap;

import com.clashroom.shared.Constant;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battle.battlers.Battler;

/**
 * Represents a stat modifier that can be attached to a {@link Battler} during battle.
 */
public abstract class Buff {
	/** Should return the name of this Buff */
	public abstract String getName();
	/** Should return a short description of this Buff */
	public abstract String getDescription();
	/** Should return the name of the image file for this Buff in the {@link Constant#IMG_ICON} directory */
	public abstract String getIcon();
	/** 
	 * Automatically called when this class is constructed to give
	 * sub-classes a change to call {@link Buff#setModifier(Stat, int, float)}
	 * as required.
	 * <p/>
	 * <b>Important</b>: If you have instance variables in base-classes, make sure
	 * to call this again after they are set. Because Java will call the super constructor
	 * before you can set your instance variables, they will not be set when the super
	 * constructor calls this method.
	 */
	public abstract void setModifiers();
	
	/**
	 * An enum for the modifiable stats of {@link Battler}s.
	 */
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
	
	/**
	 * Use this method set set a {@link Modifier} for a given stat.
	 * @param stat The Stat
	 * @param plus The amount this buff should add to this stat (can be negative)
	 * @param factor The factor this buff should multiply the stat by (can be less than 1, but not less than 0)
	 */
	protected void setModifier(Stat stat, int plus, float factor) {
		Modifier mod = modifierMap.get(stat);
		mod.plus = plus;
		mod.factor = factor;
	}
	
	/**
	 * Gets this Buff's {@link Modifier} for the given {@link Stat}
	 * @param stat The Stat
	 * @return The Modifier
	 */
	public Modifier getModifier(Stat stat) {
		return modifierMap.get(stat);
	}
	
	/**
	 * A simple class representing an addition and multiplication
	 * of a stat.
	 */
	public static class Modifier {
		protected int plus = 0;
		protected float factor = 1;
		
		/**
		 * Returns the factor by which this stat should be multiplied.
		 * Can be less than 1, but should not be less than 0.
		 */
		public float getFactor() {
			return factor;
		}
		
		/**
		 * Returns the amount by which the stat should be increased.
		 * Can be negative.
		 */
		public int getPlus() {
			return plus;
		}
	}
}
