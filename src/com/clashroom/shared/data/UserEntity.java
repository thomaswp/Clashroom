package com.clashroom.shared.data;

import java.io.Serializable;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

import com.clashroom.server.QueryUtils;

@SuppressWarnings("serial")
@PersistenceCapable
public class UserEntity implements Serializable {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private String username, firstName, lastName;
	
	@Persistent
	private String email;
	
	@Persistent
	private Long dragonId;
	
	public boolean isSetup() {
		return username != null;
	}

//	public DragonEntity getDragon(PersistenceManager pm) {
//		return QueryUtils.queryUnique(pm, DragonEntity.class, "id == %s", dragonId);
//	}
	
	@Deprecated
	public UserEntity() { }
	
	public UserEntity(String email) {
		this.email = email;
	}
	
	public UserEntity(String playerName, Long dragonId) {
		username = playerName;
		this.dragonId = dragonId;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
