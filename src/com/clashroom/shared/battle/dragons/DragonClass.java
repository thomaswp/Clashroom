package com.clashroom.shared.battle.dragons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.clashroom.shared.Constant;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.DragonEntity;

/**
 * Represents a type of Dragon which a player can choose to use.
 * <p/>
 * <b>Important</b>: if you reate a new DragonClass, it must be registered in the
 * {@link DragonClass#dragons} array. This allows each class to have a unique, constant id.
 * <p/>
 * <b>Important</b>: DragonClasses should never be constructed outside of this class. Instead use 
 * {@link DragonClass#getById(int)}.
 */
public abstract class DragonClass {

	private static int nextId = 0;
	
	//Used to give each DragonClass a unique id
	protected int id = nextId++;
	//A map of skills learned paired with level learned
	protected HashMap<Skill, Integer> skillTree = new HashMap<Skill, Integer>();
	
	public HashMap<Skill, Integer> getSkillTree() {
		return skillTree;
	}
	
	//Orders skills by their level learned
	private Comparator<Skill> skillComparator = new Comparator<Skill>() {
		@Override
		public int compare(Skill o1, Skill o2) {
			return skillTree.get(o1) - skillTree.get(o2);
		}
	};
	
	/**
	 * Gets a list of the {@link Skill}s this DragonClass
	 * learns, in order of level learned.
	 */
	public List<Skill> getSkills() {
		List<Skill> skills = new ArrayList<Skill>(); 
		skills.addAll(skillTree.keySet());
		Collections.sort(skills, skillComparator);
		return skills;
	}
	
	/**
	 * Each new DragonClass should have an entry in this array.
	 * Do no change the order, as that is what determines the unique ids.
	 */
	private final static DragonClass[] dragons = new DragonClass[] { 
		new LionDragon(),
		new MeteorDragon(),
		new DragonHatchling(),
		new SkyDragon()
	};
	
	public static int getNumDragonClasses() {
		return dragons.length;
	}
	
	/**
	 * Gets the amount of experience needed to get to the next
	 * level, given the current level.
	 * @param level The dragon's current level.
	 * @return The amount of experience needed to get to the next level
	 */
	public static int getNextLevelExp(int level) {
		return (int)(Math.pow(1.3, level - 1) * 100);
	}
	
	public static DragonClass[] getAllClasses() {
		return dragons;
	}
	
	/**
	 * Gets a {@link DragonClass} by its unique id. This may not
	 * be a unique object, but since DragonClasses are immutable,
	 * this should not be a problem.
	 * @param id The id of the DragonClass
	 * @return The DragonClass
	 */
	public static DragonClass getById(int id) {
		if (id < dragons.length) return dragons[id];
		return null;
	}
	
	public DragonClass() {
		fillSkillTree(skillTree);
	}
	
	/**
	 * Sets up a {@link DragonEntity} for the first time.
	 * Should only ever be called once.
	 */
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
	
	/** The max amount of a basic stat a {@link DragonEntity} can gain per level. */
	public static int MAX_STAT_GAIN_PER_LEVEL = 10;
	
	/**
	 * Levels up this {@link DragonEntity}, automatically gaining skills
	 * based on this DragonClass.
	 * @param entity The DragonEntity to level up
	 */
	public void levelUp(DragonEntity entity) {
		//TODO: Consider giving players some control over stat gains
		
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
	/**
	 * Randomly generates a stat gain, with some variance.
	 */
	private double generatePlus(double base, double factor) {
		factor += factor * (Math.random() - 0.5) * PLUS_RANGE;
		return base * factor;
	}
	
	/** Should return the name of the image file for this DragonClass in the {@link Constant#IMG_BATTLER} directory*/
	public abstract String getImageName();
	/** Should return the name of this DragonClass */
	public abstract String getName();
	/** Should return the description for this DragonClass, used when players are choosing their Dragon. */
	public abstract String getDescription();
	/** Should return factor [0-1] of strength gain per level for this DragonClass */
	public abstract double getStrengthFactor();
	/** Should return factor [0-1] of agility gain per level for this DragonClass */
	public abstract double getAgilityFactor();
	/** Should return factor [0-1] of intelligence gain per level for this DragonClass */
	public abstract double getIntelligenceFactor();
	/** Should return factor [0-1] of hp gain per level for this DragonClass */
	public abstract double getHpFactor();
	/** Should return factor [0-1] of mp gain per level for this DragonClass */
	public abstract double getMpFactor();
	/**
	 * Should fill the given skill tree with pairs of {@link Skill}s and the
	 * levels at which those Skills can be learned.
	 * @param skillTree The skill tree to fill
	 */
	protected abstract void fillSkillTree(HashMap<Skill, Integer> skillTree);

	/**
	 * Gets the unique id for this DragonClass.
	 */
	public int getId() {
		return id;
	}
}
