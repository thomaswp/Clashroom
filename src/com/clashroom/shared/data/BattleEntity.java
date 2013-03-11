package com.clashroom.shared.data;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.actions.ActionFinish;
import com.clashroom.shared.actions.BattleAction;

@PersistenceCapable
public class BattleEntity {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent(serialized="true")
	private BattleFactory battleFactory;
	
	@Persistent
	private boolean teamAVictor;
	
	public BattleEntity(BattleFactory battleFactory) {
		this.battleFactory = battleFactory;
		
		Battle battle = battleFactory.generateBattle();
		BattleAction action = battle.nextAction();
		while (!(action instanceof ActionFinish)) {
			action = battle.nextAction();
		}
		teamAVictor = ((ActionFinish) action).teamAVictor;
	}

	public Long getId() {
		return id;
	}

	public boolean isTeamAVictor() {
		return teamAVictor;
	}
	
	public BattleFactory getBattleFactory() {
		battleFactory.setId(id);
		return battleFactory;
	}
}
