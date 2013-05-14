package com.clashroom.shared.battle.dragons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.DragonEntity;

public abstract class DragonClass {

	private static int nextId = 0;
	
	protected int id = nextId++;
	protected HashMap<Skill, Integer> skillTree = new HashMap<Skill, Integer>();
	
	public HashMap<Skill, Integer> getSkillTree() {
		return skillTree;
	}
	
	private Comparator<Skill> skillComparator = new Comparator<Skill>() {
		@Override
		public int compare(Skill o1, Skill o2) {
			return skillTree.get(o1) - skillTree.get(o2);
		}
	};
	public List<Skill> getSkills() {
		List<Skill> skills = new ArrayList<Skill>(); 
		skills.addAll(skillTree.keySet());
		Collections.sort(skills, skillComparator);
		return skills;
	}
	
	private final static DragonClass[] dragons = new DragonClass[] { 
		new LionDragon(),
		new MeteorDragon(),
		new DragonHatchling(),
		new SkyDragon()
	};
	
	public static int getNumDragonClasses() {
		return dragons.length;
	}
	
	public static int getNextLevelExp(int level) {
		return (int)(Math.pow(1.3, level - 1) * 100);
	}
	
	public static DragonClass[] getAllClasses() {
		return dragons;
	}
	
	public static DragonClass getById(int id) {
		if (id < dragons.length) return dragons[id];
		return null;
	}
	
	public DragonClass() {
		fillSkillTree(skillTree);
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
	
	public static int MAX_STAT_GAIN_PER_LEVEL = 10;
	
	public void levelUp(DragonEntity entity) {
		
		double plusStr = generatePlus(MAX_STAT_GAIN_PER_LEVEL, getStrengthFactor());
		double plusAgi = generatePlus(MAX_STAT_GAIN_PER_LEVEL, getAgilityFactor());
		double plusInt = generatePlus(MAX_STAT_GAIN_PER_LEVEL, getIntelligenceFactor());
		double plusHp = 40 + generatePlus(60, getHpFactor());
		double plusMp = 10 + generatePlus(15, getMpFactor());
		
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
	protected abstract void fillSkillTree(HashMap<Skill, Integer> skillTree);

	public int getId() {
		return id;
	}
}
