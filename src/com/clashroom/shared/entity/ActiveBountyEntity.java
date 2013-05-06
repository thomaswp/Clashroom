package com.clashroom.shared.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.clashroom.shared.task.ActiveTaskList;

@PersistenceCapable
public class ActiveBountyEntity {
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent(serialized="true")
	private ActiveTaskList activeQuests;
	
	@Persistent
	private Long userID;
	
	public ActiveBountyEntity() {
		activeQuests = new ActiveTaskList();
	}
	
	public ActiveBountyEntity(ActiveTaskList aql) {
		activeQuests = aql;
	}
	
	public ActiveTaskList getActiveQuests(){
		return activeQuests;
	}
	
	public void setActiveQuests(ActiveTaskList aql){
		activeQuests = aql;
	}
	
	public Long getId(){
		return id;
	}
	
	public Long getUserId(){
		return userID;
	}
	
	public void setUser(long id){
		userID = id;
	}
}
