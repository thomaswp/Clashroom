package com.clashroom.shared.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

import com.clashroom.shared.battlers.Battler;

@PersistenceCapable
public class BattleEntity {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private Long dragonId;

	@Persistent(serialized="true")
	private Battler battler;
	
	public BattleEntity(Battler battle) {
		this.battler = battle;
	}

	public Long getDragonId() {
		return dragonId;
	}

	public void setDragonId(Long dragonId) {
		this.dragonId = dragonId;
	}

	public Battler getBattler() {
		return battler;
	}

	public void setBattler(Battler battler) {
		this.battler = battler;
	}

	public Long getId() {
		return id;
	}
}
