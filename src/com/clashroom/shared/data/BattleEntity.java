package com.clashroom.shared.data;


import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.actions.ActionFinish;
import com.clashroom.shared.actions.BattleAction;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.battlers.DragonBattler;

@SuppressWarnings("serial")
@PersistenceCapable
public class BattleEntity implements Serializable {

	public final static double LOSE_EXP_FACTOR = 0.25;
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent(serialized="true")
	private BattleFactory battleFactory;
	
	@Persistent
	private boolean teamAVictor;
	
	@Persistent
	private Date date;
	
	@Persistent
	private LinkedList<Long> playerIds = new LinkedList<Long>();
	
	@Deprecated
	public BattleEntity() { }
	
	public BattleEntity(BattleFactory battleFactory) {
		this.battleFactory = battleFactory;
		
		Battle battle = battleFactory.generateBattle();
		BattleAction action = battle.nextAction();
		while (!(action instanceof ActionFinish)) {
			action = battle.nextAction();
		}
		teamAVictor = ((ActionFinish) action).teamAVictor;
		
		date = new Date();
		
		for (Battler battler : battleFactory.getTeamA()) {
			if (battler instanceof DragonBattler) {
				playerIds.add(((DragonBattler) battler).playerId);
			}
		}
		for (Battler battler : battleFactory.getTeamB()) {
			if (battler instanceof DragonBattler) {
				playerIds.add(((DragonBattler) battler).playerId);
			}
		}
	}

	public Long getId() {
		return id;
	}

	public boolean isTeamAVictor() {
		return teamAVictor;
	}
	
	public BattleFactory getBattleFactory() {
		return battleFactory;
	}

	public Date getDate() {
		return date;
	}
	
}
