package com.clashroom.shared.battle.battlers;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.Battle;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.UserEntity;

/**
 * A {@link Battler} which takes its stats for a {@link DragonEntity}
 * to represent a player in a {@link Battle}.
 */
public class DragonBattler extends Battler {
	private static final long serialVersionUID = 1L;
	
	public long playerId;
	
	//Empty constructor is necessary for passing over RPC
	@Deprecated
	public DragonBattler() { }
	
	/**
	 * Constructs this {@link DragonBattler} using the given {@link DragonEntity}
	 * and the id of the owning {@link UserEntity}.
	 * @param dragon The DragonEntitiy
	 * @param playerId The UserEntity's id
	 */
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
		//add this in to make sure it's copied
		db.playerId = playerId;
		return db;
	}

}
