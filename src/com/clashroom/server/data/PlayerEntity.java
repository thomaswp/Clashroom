package com.clashroom.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class PlayerEntity {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private String name;
	
	@Persistent(serialized="true")
	private Dragon dragon;

	public PlayerEntity(String playerName, String dragonName) {
		name = playerName;
		dragon = new Dragon(dragonName);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dragon getDragon() {
		return dragon;
	}

	public void setDragon(Dragon dragon) {
		this.dragon = dragon;
	}

	public Long getId() {
		return id;
	}
	
	
	
}
