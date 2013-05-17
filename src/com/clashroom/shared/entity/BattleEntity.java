package com.clashroom.shared.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

import com.clashroom.shared.battle.Battle;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.actions.ActionFinish;
import com.clashroom.shared.battle.actions.BattleAction;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.battlers.DragonBattler;

/**
 * An Entity wrapping a {@link BattleFactory} and
 * exposing the important fields. Also handles
 * calculating experience gain for battling Dragons.
 */
@PersistenceCapable
public class BattleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//experience decrease for the losers
	public final static double LOSE_EXP_FACTOR = 0.75;
	//experience decrease for not killing a dragon
	public final static double LIVE_EXP_FACTOR = 0.30;
	
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
	
	@Persistent
	private LinkedList<Long> teamAIds = new LinkedList<Long>();
	
	@Persistent
	private LinkedList<Long> teamBIds = new LinkedList<Long>();
	
	@Persistent
	private Integer teamAExp = 0;
	
	@Persistent
	private Integer teamBExp = 0;
	
	@Deprecated
	public BattleEntity() { }
	
	public BattleEntity(BattleFactory battleFactory, Date date) {
		this.battleFactory = battleFactory;
		this.date = date;
		
		//Determine who won the battle
		Battle battle = battleFactory.generateBattle();
		BattleAction action = battle.nextAction();
		while (!(action instanceof ActionFinish)) {
			action = battle.nextAction();
		}
		teamAVictor = ((ActionFinish) action).teamAVictor;
		
		//Calculate teamA and teamB's exp
		for (Battler battler : battle.getTeamB()) {
			int exp = battler.getExpReward();
			if (!battler.isDead()) exp *= LIVE_EXP_FACTOR;
			teamAExp += exp;
		}
		if (!teamAVictor) teamAExp = (int)(teamAExp * LOSE_EXP_FACTOR);
		for (Battler battler : battle.getTeamA()) {
			int exp = battler.getExpReward();
			if (!battler.isDead()) exp *= LIVE_EXP_FACTOR;
			teamBExp += exp;
		}
		if (teamAVictor) teamBExp = (int)(teamBExp * LOSE_EXP_FACTOR);
		
		//Add the teams' id's so this can be looked up in the datastore with them
		for (Battler battler : battleFactory.getTeamA()) {
			if (battler instanceof DragonBattler) {
				teamAIds.add(((DragonBattler) battler).playerId);
			}
		}
		for (Battler battler : battleFactory.getTeamB()) {
			if (battler instanceof DragonBattler) {
				teamBIds.add(((DragonBattler) battler).playerId);
			}
		}
		playerIds.addAll(teamAIds);
		playerIds.addAll(teamBIds);
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

	public Integer getTeamAExp() {
		return teamAExp;
	}

	public Integer getTeamBExp() {
		return teamBExp;
	}

	public LinkedList<Long> getTeamAIds() {
		return teamAIds;
	}

	public LinkedList<Long> getTeamBIds() {
		return teamBIds;
	}
	
}
