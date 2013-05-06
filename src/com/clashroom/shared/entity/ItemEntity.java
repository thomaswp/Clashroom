package com.clashroom.shared.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
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

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	public ItemEntity(){
		
	}
	
	public ItemEntity(String aName, String aDescription){
		this.name = aName;
		this.desctiption = aDescription;
	}
}
