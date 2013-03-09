package com.clashroom.shared;

import java.io.Serializable;
import java.util.List;

import com.clashroom.shared.battlers.Battler;

public class BattleFactory implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private List<Battler> teamA, teamB;
	private String teamAName, teamBName;
	private long seed;
	
	public String getName() {
		return Formatter.format("%s v %s", teamAName, teamBName);
	}
	
	public Battle generateBattle() {
		return new Battle(teamAName, teamA, 
				teamBName, teamB, seed);
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
