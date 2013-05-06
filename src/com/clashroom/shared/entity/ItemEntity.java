package com.clashroom.shared.entity;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.clashroom.shared.battle.skills.AttackSkill;
import com.clashroom.shared.battle.skills.FireBreathSkill;
import com.clashroom.shared.battle.skills.FireballSkill;
import com.clashroom.shared.battle.skills.HealSkill;
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
	private String imageDir;

	@Persistent
	private ItemType itemType;
	
	@Persistent
	private String desctiption;
	
	@Persistent(serialized = "true")
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

	public ItemEntity(String aName, String aDescription,
			Skill aSkill,ItemType theitemType,String imageDir){
		this.name = aName;
		this.desctiption = aDescription;
		this.skill = aSkill;
		this.itemType = theitemType;
		this.imageDir = "/img/itemIMG/"+imageDir;
	}
	
	public void setSkill(Skill aSkill){
		skill = aSkill;
	}
	
	public Skill getSkill(){
		return skill;
	}
	
	public String getImageDir() {
		return imageDir;
	}

	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
}
