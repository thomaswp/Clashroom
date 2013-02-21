package com.clashroom.shared;

import com.clashroom.shared.data.DragonEntity;

public class DragonBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	public DragonBattler(DragonEntity dragon) {
		name = dragon.getName();
		level = dragon.getLevel();
		strength = dragon.getStrength();
		agility = dragon.getAgility();
		intelligence = dragon.getIntelligence();
		description = dragon.getName();
	}

}
