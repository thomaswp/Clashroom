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

@PersistenceCapable
public class BattleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static double LOSE_EXP_FACTOR = 0.50;
	
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
	private Integer teamAExp = 0;
	
	@Persistent
	private Integer teamBExp = 0;
	
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
		
		for (Battler battler : battle.getTeamB()) {
			if (battler.isDead()) {
				teamAExp += battler.getExpReward();
			}
		}
		if (!teamAVictor) teamAExp = (int)(teamAExp * LOSE_EXP_FACTOR);
		for (Battler battler : battle.getTeamA()) {
			if (battler.isDead()) {
				teamBExp += battler.getExpReward();
			}
		}
		if (teamAVictor) teamBExp = (int)(teamBExp * LOSE_EXP_FACTOR);
		
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

	public Integer getTeamAExp() {
		return teamAExp;
	}

	public Integer getTeamBExp() {
		return teamBExp;
	}
	
}
