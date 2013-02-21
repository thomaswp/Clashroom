package com.clashroom.shared;

import java.io.Serializable;

public abstract class Battler implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String name;
	public int level;
	public String description;
	public int strength, agility, intelligence;
	public int maxHP, maxMP;
	public int hp, mp;
	
	public Battler() {
		generateMaxHP();
		generateMaxMP();
		setup();
	}
	
	public void setup() {
		hp = maxHP;
		mp = maxMP;
	}
	
	protected void generateMaxHP() {
		maxHP = level * 85 + strength * 23;
	}
	
	protected void generateMaxMP() {
		maxMP = level * 25 + intelligence * 18;
	}
	
	protected int getStatCurve(int level, int minGain, int maxGain) {
		return (int)((Math.random() * (maxGain - minGain) + minGain) * (level + 3));
	}
}
