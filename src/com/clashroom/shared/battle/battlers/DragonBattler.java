package com.clashroom.shared.battle.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.skills.ActiveSkill;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.DragonEntity;

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
		description = getDescription();
		
		for (int id : dragon.getSkills()) {
			skills.add(Skill.getById(id));
		}
	}
	
	

	@Override
	protected String getDescription() {
		return Formatter.format("%s, Level %d", 
				name, level);
	}

	@Override
	public Battler copyChild() {
		DragonBattler db = new DragonBattler();
		db.playerId = playerId;
		return db;
	}

}
