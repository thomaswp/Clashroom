package com.clashroom.shared.dragons;

import com.clashroom.shared.data.DragonEntity;

public abstract class DragonClass {

	private static int nextId = 0;
	protected int id = nextId++;
	
	//3 total stats
	
	private final static DragonClass[] dragons = new DragonClass[] { 
		new LionDragon(),
		new MeteorDragon(),
		new DragonHatchling(),
		new SkyDragon()
	};
	
	public static DragonClass[] getAllClasses() {
		return dragons;
	}
	
	public static DragonClass getById(int id) {
		if (id < dragons.length) return dragons[id];
		return null;
	}
	
	public void setUp(DragonEntity entity) {
		entity.setStrength(5);
		entity.setAgility(5);
		entity.setIntelligence(5);
		entity.setMaxHp(200);
		entity.setMaxMp(50);
		
		for (int i = 0; i < 2; i++) {
			levelUp(entity);
		}
		
	}
	
	public void levelUp(DragonEntity entity) {
		
		double plusStr = generatePlus(10, getStrengthFactor());
		double plusAgi = generatePlus(10, getAgilityFactor());
		double plusInt = generatePlus(10, getIntelligenceFactor());
		double plusHp = generatePlus(100, getHpFactor());
		double plusMp = generatePlus(25, getMpFactor());
		
		entity.setStrength(entity.getStrength() + plusStr);
		entity.setAgility(entity.getAgility() + plusAgi);
		entity.setIntelligence(entity.getIntelligence() + plusInt);
		entity.setMaxHp(entity.getMaxHp() + plusHp);
		entity.setMaxMp(entity.getMaxMp() + plusMp);
	}
	
	private final static double PLUS_RANGE = 0.3;
	private double generatePlus(double base, double factor) {
		factor += factor * (Math.random() - 0.5) * PLUS_RANGE;
		return base * factor;
	}
	
	public abstract String getImageName();
	public abstract String getName();
	public abstract String getDescription();
	public abstract double getStrengthFactor();
	public abstract double getAgilityFactor();
	public abstract double getIntelligenceFactor();
	public abstract double getHpFactor();
	public abstract double getMpFactor();

	public int getId() {
		return id;
	}
}
