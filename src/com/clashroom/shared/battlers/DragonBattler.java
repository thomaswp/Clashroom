package com.clashroom.shared.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.dragons.DragonClass;

public class DragonBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	public DragonBattler(DragonEntity dragon) {
		DragonClass dragonClass = DragonClass.getById(dragon.getDragonClassId());
		
		name = dragon.getName();
		image = dragonClass.getImageName();
		level = dragon.getLevel();
		strength = (int)dragon.getStrength();
		agility = (int)dragon.getAgility();
		intelligence = (int)dragon.getIntelligence();
		maxHp = (int)dragon.getMaxHp();
		maxMp = (int)dragon.getMaxMp();
		description = Formatter.format("Level %d %s", 
				dragon.getLevel(), dragonClass.getName());
	}

}
