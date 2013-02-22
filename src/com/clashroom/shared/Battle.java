package com.clashroom.shared;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Battle {
	private LinkedList<Battler> battlers = new LinkedList<Battler>();
	private LinkedList<Battler> teamA = new LinkedList<Battler>();
	private LinkedList<Battler> teamB = new LinkedList<Battler>();
	private LinkedList<BattleAction> queuedActions = new LinkedList<BattleAction>();
	private boolean isOver;
	
	public boolean isOver() {
		return isOver;
	}
	
	public List<Battler> getBattlers() {
		return battlers;
	}
	
	public List<Battler> getTeamA() {
		return teamA;
	}
	
	public List<Battler> getTeamB() {
		return teamB;
	}
	
	public Battle(LinkedList<Battler> teamA, LinkedList<Battler> teamB) {
		this.teamA = teamA;
		this.teamB = teamB;
		
		for (Battler b : teamA) b.teamA = true;
		for (Battler b : teamB) b.teamA = false;
		
		battlers.addAll(teamA);
		battlers.addAll(teamB);
		Collections.sort(battlers, new Comparator<Battler>() {
			@Override
			public int compare(Battler o1, Battler o2) {
				return o1.agility = o2.agility;
			}
		});
		
		for (Battler b : battlers) {
			b.setup();
		}
	}
	
	public String getStatus() {
		return battlers.toString();
	}
	
	public BattleAction nextAction() {
		if (queuedActions.size() > 0) {
			return queuedActions.removeFirst();
		}
		
		Battler attacker = battlers.removeFirst();
		
		if (battlers.size() == 0) {
			isOver = true;
			return new ActionFinish(attacker);
		}
		
		Battler target = attacker.selectTarget(getEnemies(attacker));
		BattleAction attack = doAttack(attacker, target);
		
		if (target.hp <= 0) {
			kill(target);
			queuedActions.add(new ActionDeath(target));
		}
		
		battlers.add(attacker);
		
		return attack;
	}
	
	private void kill(Battler battler) {
		battlers.remove(battler);
		teamA.remove(battler);
		teamB.remove(battler);
	}
	
	private LinkedList<Battler> getAllies(Battler battler) {
		return battler.teamA ? teamA : teamB;
	}
	
	private LinkedList<Battler> getEnemies(Battler battler) {
		return battler.teamA ? teamB : teamA;
	}
	
	private ActionAttack doAttack(Battler attacker, Battler target) {
		boolean miss = Math.sqrt(attacker.agility / (double)target.agility) < Math.random() * 1.5;
		int damage = 0;
		if (!miss) {
			damage = (attacker.strength - target.strength / 4) * 3;
		}
		target.hp = Math.max(0, target.hp - damage);
		return new ActionAttack(attacker, target, damage, miss);
	}
}
