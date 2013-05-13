package com.clashroom.shared.battle;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.actions.ActionDeath;
import com.clashroom.shared.battle.actions.ActionFinish;
import com.clashroom.shared.battle.actions.ActionSkill;
import com.clashroom.shared.battle.actions.ActionSkillTargetAll;
import com.clashroom.shared.battle.actions.BattleAction;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.battlers.DragonBattler;
import com.clashroom.shared.battle.skills.ActiveSkill;
import com.clashroom.shared.battle.skills.ActiveSkill.Target;
import com.clashroom.shared.entity.UserEntity;

public class Battle {
	private Random random;
	private LinkedList<Battler> battlers = new LinkedList<Battler>();
	private String teamAName, teamBName;
	private List<Battler> teamA, teamB;
	private LinkedList<Battler> teamALiving = new LinkedList<Battler>();
	private LinkedList<Battler> teamBLiving = new LinkedList<Battler>();
	private LinkedList<BattleAction> queuedActions = new LinkedList<BattleAction>();
	private boolean isOver;
	private List<BattleAction> postBattleActions;
	
	private LinkedList<Battler> tempBattlers = new LinkedList<Battler>();
	
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
	
	public static String getTeamName(List<UserEntity> team) {
		String teamName = "";
		for (UserEntity userEntity : team) {
			DragonBattler db = new DragonBattler(userEntity.getDragon(), userEntity.getId());
			teamName = Formatter.appendList(teamName, db.name);
		}
		return teamName;
	}
	
	public BattleAction getNextPostBattleAction() {
		if (postBattleActions.size() == 0) return null;
		return postBattleActions.remove(0);
	}
	
	public Battle(String teamAName, List<Battler> teamA, 
			String teamBName, List<Battler> teamB, long seed, 
			List<BattleAction> postBattleActions) {
		random = new Random(seed);
		this.postBattleActions = postBattleActions;
		
		this.teamA = teamA;
		this.teamB = teamB;
		this.teamAName = teamAName;
		this.teamBName = teamBName;
		
		for (Battler b : teamA) b.teamA = true;
		for (Battler b : teamB) b.teamA = false;
		
		teamALiving.addAll(teamA);
		teamBLiving.addAll(teamB);
		
		battlers.addAll(teamA);
		battlers.addAll(teamB);
		Collections.sort(battlers, new Comparator<Battler>() {
			@Override
			public int compare(Battler o1, Battler o2) {
				return o1.getAgility() - o2.getAgility();
			}
		});
		
		adjustNames(battlers);
		
		for (Battler b : battlers) {
			b.setup();
		}
	}
	
	private void adjustNames(LinkedList<Battler> battlers) {
		for (int i = 0; i < battlers.size(); i++) {
			Battler b1 = battlers.get(i);
			String name = b1.name;
			char letter = 'A';
			for (int j = i + 1; j < battlers.size(); j++) {
				Battler b2 = battlers.get(j);
				if (name.equals(b2.name)) {
					if (letter == 'A') {
						b1.name += " " + letter++;
					}
					b2.name += " " + letter++;
				}
			}
		}
	}

	public String getStatus() {
		return battlers.toString();
	}
	
	public BattleAction nextAction() {
		if (queuedActions.size() > 0) {
			BattleAction action =  queuedActions.removeFirst();
			//Debug.write(action.toBattleString());
			return action;
		}
		
		Battler attacker = battlers.get(0);
		
		if (teamALiving.size() == 0 || teamBLiving.size() == 0) {
			isOver = true;
			boolean teamAVictor = teamBLiving.size() == 0;
			return new ActionFinish(teamAVictor, teamAVictor ? teamAName : teamBName);
		}

		List<Battler> allies = getLivingAllies(attacker);
		List<Battler> enemies = getLivingEnemies(attacker);
		
		ActiveSkill skill = attacker.selectSkill(random, allies, enemies);
		List<Battler> targets;
		Battler target = null;
		if (skill.targetsAllies()) {
			targets = allies;
			target = attacker.selectAllyTarget(targets, skill, random);
		} else {
			targets = enemies;
			target = attacker.selectEnemyTarget(targets, skill, random);	
		}
		
		
		BattleAction action;
		if (skill.getTarget() == Target.Self) {
			ActionSkill attack = skill.getAttack(attacker, attacker, random);
			action = attack;
			doDamage(attack.getPrimaryDamage());
		} else if (skill.getTarget() == Target.One) {
			ActionSkill attack = skill.getAttack(attacker, target, random);
			action = attack;
			doDamage(attack.getPrimaryDamage());
		} else if (skill.getTarget() == Target.Splash) {
			ActionSkill attack = skill.getAttack(attacker, target, random);
			action = attack;
			doDamage(attack.getPrimaryDamage());
			if (!attack.missed) {
				List<Battler> allTargets = skill.targetsAllies() ? getAllAllies(attacker) :
					getAllEnemies(attacker);
				int index = allTargets.indexOf(target);
				for (int i = index - 1; i < index + 2; i += 2) {
					if (i >= 0 && i < allTargets.size() && !allTargets.get(i).isDead()) {
						Damage damage = skill.getDamage(attacker, allTargets.get(i), random);
						damage.damage *= 0.5;
						attack.damages.add(damage);
						doDamage(damage);
					}
				}
			}
		} else {
			LinkedList<ActionSkill> attacks = new LinkedList<ActionSkill>();
			boolean critical = skill.getCritical(attacker, random);
			for (Battler battler : targets) {
				ActionSkill oneAttack = skill.getAttack(attacker, battler, critical, random);
				attacks.add(oneAttack);
				doDamage(oneAttack.getPrimaryDamage());
			}
			action = new ActionSkillTargetAll(attacker, skill, critical, attacks);
		}
		attacker.mp -= skill.getMpCost();
		
		tempBattlers.clear();
		for (Battler b : battlers) {
			if (b.hp <= 0) {
				tempBattlers.add(b);
			}
		}
		for (Battler b : tempBattlers) {
			kill(b);
			queuedActions.add(new ActionDeath(b));
		}
		
		battlers.removeFirst();
		battlers.add(attacker);
		
		return action;
	}
	
	private void doDamage(Damage damage) {
		if (damage != null) {
			Battler target = damage.target;
			target.hp = Math.max(0, target.hp - damage.damage);
			target.hp = Math.min(target.hp, target.getMaxHp());
			if (damage.buff != null) {
				target.buffs.add(damage.buff);
			}
		}
	}
	
	
	private void kill(Battler battler) {
		battlers.remove(battler);
		teamALiving.remove(battler);
		teamBLiving.remove(battler);
	}
	
	private LinkedList<Battler> getLivingAllies(Battler battler) {
		return battler.teamA ? teamALiving : teamBLiving;
	}
	
	private LinkedList<Battler> getLivingEnemies(Battler battler) {
		return battler.teamA ? teamBLiving : teamALiving;
	}
	
	private List<Battler> getAllAllies(Battler battler) {
		return battler.teamA ? teamA: teamB;
	}
	
	private List<Battler> getAllEnemies(Battler battler) {
		return battler.teamA ? teamB: teamA;
	}
}
