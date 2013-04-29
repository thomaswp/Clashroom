package com.clashroom.shared.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.clashroom.shared.battle.skills.Skill;
/*
 * Item Entity, the teacher will be allowed to make items for students
 * to obtain
 * 
 * More will be added to this class eventually. Need ideas
 * 
 * Riese Narcisse
 */
@PersistenceCapable
public class ItemEntity implements Serializable {
	
	@Persistent
	private String name;
	
	@Persistent
	private String desctiption;
	
	@Persistent
	private Skill skill;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	public ItemEntity(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesctiption() {
		return desctiption;
	}

	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ItemEntity(String aName, String aDescription){
		this.name = aName;
		this.desctiption = aDescription;
	}
	
	public void setSkill(Skill aSkill){
		skill = aSkill;
	}
	
	public Skill getSkill(){
		return skill;
	}
}
