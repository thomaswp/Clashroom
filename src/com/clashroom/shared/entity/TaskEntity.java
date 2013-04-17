package com.clashroom.shared.entity;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


import com.clashroom.shared.task.Task;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class TaskEntity {
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private String title;
	
	@Persistent
	private Text desc;
	
	@Persistent
	private int duration, reward;
	
	public TaskEntity(Task q){
		this.title = q.getTitle();
		this.desc = new Text(q.getDesc());
		this.duration = q.getDuration();
		this.reward = q.getReward();
	}
	
	public TaskEntity(String title, String desc, int duration, int reward) {
		this.title = title;
		this.desc = new Text(desc);
		this.duration = duration;
		this.reward = reward;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc.getValue();
	}

	public void setDesc(String desc) {
		this.desc = new Text(desc);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

}
