package com.clashroom.shared.data;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

import com.clashroom.server.QueryUtils;

@PersistenceCapable
public class PlayerEntity {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private Long dragonId;

	public DragonEntity getDragon(PersistenceManager pm) {
		return QueryUtils.queryUnique(pm, DragonEntity.class, "id == %s", dragonId);
	}
	
	public PlayerEntity(String playerName, Long dragonId) {
		name = playerName;
		this.dragonId = dragonId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public Long getDragonId() {
		return dragonId;
	}

	public void setDragonId(Long dragonId) {
		this.dragonId = dragonId;
	}
}
