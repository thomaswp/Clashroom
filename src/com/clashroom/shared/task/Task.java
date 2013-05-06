package com.clashroom.shared.task;

import java.io.Serializable;

import com.clashroom.shared.entity.TaskEntity;

public class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String desc;
	private int duration;
	private int reward;
	
	public Task(TaskEntity qe){
		this.title = qe.getTitle();
		this.desc = qe.getDesc();
		this.duration = qe.getDuration();
		this.reward = qe.getReward();
	}
	
	public Task(Task q) {
		this.title = q.getTitle();
		this.desc = q.getDesc();
		this.duration = q.getDuration();
		this.reward = q.getReward();
	}
	
	public Task(String title, String desc, int duration, int reward) {
		this.title = title;
		this.desc = desc;
		this.duration = duration;
		this.reward = reward;
	}
	
	public Task() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
