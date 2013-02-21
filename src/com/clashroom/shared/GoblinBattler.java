package com.clashroom.shared;

public class GoblinBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	public GoblinBattler(int level) {
		name = "Goblin";
		this.level = level;
		strength = getStatCurve(level, 3, 5);
		agility = getStatCurve(level, 2, 4);
		intelligence = getStatCurve(level, 1, 2);
		description = String.format("Level %d Goblin", level);
	}
	
}
