package com.clashroom.shared;

public class ActionAttack extends BattleAction {
	public Battler attacker, attacked;
	public int damage;
	public boolean missed;
	
	public ActionAttack(Battler attacker, Battler attacked, int damage,
			boolean missed) {
		super();
		this.attacker = attacker;
		this.attacked = attacked;
		this.damage = damage;
		this.missed = missed;
	}

	@Override
	public String toBattleString() {
		String damageString = missed ? "and missed" : String.format("dealing %d damage", damage);
		return String.format("%s attacked %s %s!", attacker.name, attacked.name, damageString);
	}
}
