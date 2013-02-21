package com.clashroom.shared;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Battle {
	private Battler battlerA, battlerB;
	private LinkedList<Battler> battlers = new LinkedList<Battler>();
	private LinkedList<BattleAction> queuedActions = new LinkedList<BattleAction>();
	private boolean isOver;
	
	public boolean isOver() {
		return isOver;
	}
	
	public Battle(Battler battlerA, Battler battlerB) {
		this.battlerA = battlerA;
		this.battlerB = battlerB;
		
		battlers.add(battlerA);
		battlers.add(battlerB);
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
		
		Battler target = attacker.selectTarget(battlers);
		BattleAction attack = doAttack(attacker, target);
		
		if (target.hp <= 0) {
			battlers.remove(target);
			queuedActions.add(new ActionDeath(target));
		}
		
		battlers.add(attacker);
		
		return attack;
	}
	
	private ActionAttack doAttack(Battler attacker, Battler target) {
		boolean miss = attacker.agility / (double)target.agility < Math.random() * 1.5;
		int damage = 0;
		if (!miss) {
			damage = attacker.strength - target.strength / 4;
		}
		target.hp = Math.max(0, target.hp - damage);
		return new ActionAttack(attacker, target, damage, miss);
	}
}
