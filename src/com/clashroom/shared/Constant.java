package com.clashroom.shared;

/**
 * A class for storing path and text constants. 
 */
public class Constant {
	/** The path to the image directory */
	public final static String IMG = "img/";
	/** The path to the battler image directory */
	public final static String IMG_BATTLER = IMG + "battler/";
	/** The path to the red battler image directory */
	public final static String IMG_BATTLER_RED = IMG_BATTLER + "red/";
	/** The path to the green battler image directory */
	public final static String IMG_BATTLER_GREEN = IMG_BATTLER + "green/";
	/** The path to the icon image directory */
	public final static String IMG_ICON = IMG + "icon/";
	
	//Change the follow if you want to use more imaginative
	//or possible more accessible words for the stats in the game
	/** A constant for the health stat */
	public final static String STAT_HP = "HP";
	/** A constant for the manna stat */
	public final static String STAT_MP = "MP";
	/** A constant for the strength stat */
	public final static String STAT_STR = "Strength";
	/** A constant for the agility stat */
	public final static String STAT_AGI = "Agility";
	/** A constant for the intelligence stat */
	public final static String STAT_INT = "Intelligence";
	
	/** The term for experience in the game */
	public final static String TERM_EXP = "Experience";
	/** The term for experience's short abbreviation in the game */
	public final static String TERM_EXP_SHORT = "Exp";
	/** The term for skill points in the game */
	public final static String TERM_SKILL_POINT = "Quest Point";
	/** The term for skill points' short abbreviation in the game */
	public final static String TERM_SKILL_POINT_SHORT = "qp";
	/** The term for a skill in the game */
	public final static String TERM_SKILL = "Skill";
	/** The term for plural skills in the game */
	public final static String TERM_SKILL_PL = "Skills";
}
