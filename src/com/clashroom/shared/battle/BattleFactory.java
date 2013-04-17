package com.clashroom.shared.battle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.actions.BattleAction;
import com.clashroom.shared.battle.battlers.Battler;

public class BattleFactory implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Battler> teamA, teamB;
	private String teamAName, teamBName;
	private long seed;
	private List<BattleAction> postBattleActions =
			new LinkedList<BattleAction>();
	
	public String getName() {
		return Formatter.format("%s v %s", teamAName, teamBName);
	}
		
	public String getTeamAName() {
		return teamAName;
	}

	public String getTeamBName() {
		return teamBName;
	}
	
	public List<Battler> getTeamA() {
		return teamA;
	}
	
	public List<Battler> getTeamB() {
		return teamB;
	}

	public Battle generateBattle() {
		return new Battle(teamAName, teamA, 
				teamBName, teamB, seed, postBattleActions);
	}
	
	public void addPostBattleAction(BattleAction battleAction) {
		postBattleActions.add(battleAction);
	}
	
	public BattleFactory(String teamAName, List<Battler> teamA, 
			String teamBName, List<Battler> teamB) {
		this.teamAName = teamAName;
		this.teamA = teamA;
		this.teamBName = teamBName;
		this.teamB = teamB;
		
		seed = (long)(Math.random() * Long.MAX_VALUE);
	}
	
	public BattleFactory() { }
}
