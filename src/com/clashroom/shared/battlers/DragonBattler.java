package com.clashroom.shared.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.dragons.DragonClass;
import com.clashroom.shared.skills.Skill;

public class DragonBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	public long playerId;
	
	@Deprecated
	public DragonBattler() { }
	
	public DragonBattler(DragonEntity dragon, long playerId) {
		DragonClass dragonClass = DragonClass.getById(dragon.getDragonClassId());
		
		this.playerId = playerId;
		
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
		
		for (Skill skill : dragonClass.getSkillTree().keySet()) {
			int level = dragonClass.getSkillTree().get(skill);
			if (level <= this.level) {
				skills.add(skill);
			}
		}
	}

}
