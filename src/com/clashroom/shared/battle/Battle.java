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

/**
 * Represents a battle between 2 of more {@link Battler}s.
 * Note that a Battle is meant to be disposable, thrown away after use.
 * It is also not serializable. To transport a Battle, use its containing
 * {@link BattleFactory}.
 */
public class Battle {
	//It is imperative to only use this random for calculations, so the
	//battle traspires the same way each time.
	private Random random;
	private LinkedList<Battler> battlers = new LinkedList<Battler>();
	private String teamAName, teamBName;
	private List<Battler> teamA, teamB;
	private LinkedList<Battler> teamALiving = new LinkedList<Battler>();
	private LinkedList<Battler> teamBLiving = new LinkedList<Battler>();
	//Actions created during the last turn that still need to be posted,
	//such as when a player attacks and kills another player (2 actions in 1 turn)
	private LinkedList<BattleAction> queuedActions = new LinkedList<BattleAction>();
	private boolean isOver;
	private List<BattleAction> postBattleActions;
	
	private LinkedList<Battler> tempBattlers = new LinkedList<Battler>();
	
	/**
	 * Returns true if this Battle has no more Actions to generated and 
	 * team has won. The Battle may have more PostBattleActions still.
	 */
	public boolean isOver() {
		return isOver;
	}
	
	/**
	 * Gets all {@link Battler}s participating in this battle.
	 */
	public List<Battler> getBattlers() {
		return battlers;
	}
	
	/**
	 * Gets all {@link Battle}s on teamA.
	 */
	public List<Battler> getTeamA() {
		return teamA;
	}
	
	/**
	 * Gets all {@link Battle}s on teamB.
	 */
	public List<Battler> getTeamB() {
		return teamB;
	}
	
	/**
	 * Generates a name for a team, given a list of {@link UserEntity}s on that team.
	 * @param team The battlers
	 * @return The name
	 */
	public static String getTeamName(List<UserEntity> team) {
		String teamName = "";
		for (UserEntity userEntity : team) {
			DragonBattler db = new DragonBattler(userEntity.getDragon(), userEntity.getId());
			teamName = Formatter.appendList(teamName, db.name);
		}
		return teamName;
	}
	
	/**
	 * Pops a {@link BattleAction} that occurs after the {@link Battle#isOver}.
	 */
	public BattleAction getNextPostBattleAction() {
		if (postBattleActions.size() == 0) return null;
		return postBattleActions.remove(0);
	}
	
	/**
	 * Constructs a Battle using the given teams and a random seed.
	 * @param teamAName The names of TeamA
	 * @param teamA The Battlers in TeamA
	 * @param teamBName The names of TeamB
	 * @param teamB The Battlers in TeamB 
	 * @param seed The random seed to start this Battle
	 * @param postBattleActions Any BattleActions to add after this
	 * Battle has finished, such as experience gain, leveling up, etc.
	 */
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
	
	//If two Battlers have the same name, it adds a distinguishing letter
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

	//For debugging, prints all the Battlers
	public String getStatus() {
		return battlers.toString();
	}
	
	/**
	 * Generates the next {@link BattleAction} for this battle, advancing
	 * the state of the Battle in the process.
	 * @return The action
	 */
	public BattleAction nextAction() {
		//If we have queuedActions from last turn, pop those first
		if (queuedActions.size() > 0) {
			BattleAction action =  queuedActions.removeFirst();
			return action;
		}
		
		//The battlers are in order of who attacks next
		Battler attacker = battlers.get(0);
		
		//End the battle if one team is empty
		if (teamALiving.size() == 0 || teamBLiving.size() == 0) {
			isOver = true;
			boolean teamAVictor = teamBLiving.size() == 0;
			return new ActionFinish(teamAVictor, teamAVictor ? teamAName : teamBName);
		}

		List<Battler> allies = getLivingAllies(attacker);
		List<Battler> enemies = getLivingEnemies(attacker);
		
		//Have the attacker select a skill to attack with
		ActiveSkill skill = attacker.selectSkill(random, allies, enemies);
		
		//Determine which battlers the skill targets
		List<Battler> targets;
		Battler target = null;
		if (skill.targetsAllies()) {
			targets = allies;
			target = attacker.selectAllyTarget(targets, skill, random);
		} else {
			targets = enemies;
			target = attacker.selectEnemyTarget(targets, skill, random);	
		}
		
		//Play out the damage
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
				//Deal splash damage to the nearby actors if the attack didn't miss
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
			//Target-all attacks
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
		
		//Kill any battler at 0 hp
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
		
		//pop the attacker and add it to the end of the queue
		battlers.removeFirst();
		battlers.add(attacker);
		
		return action;
	}
	
	//deal damage to a battler
	private void doDamage(Damage damage) {
		if (damage != null) {
			Battler target = damage.target;
			target.hp = Math.max(0, target.hp - damage.damage);
			//bound upper bound as well, in case this is healing
			target.hp = Math.min(target.hp, target.getMaxHp());
			if (damage.buff != null) {
				target.buffs.add(damage.buff);
			}
		}
	}
	
	//kill a battler at 0 hp
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
