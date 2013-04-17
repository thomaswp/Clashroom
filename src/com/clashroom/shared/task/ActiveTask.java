package com.clashroom.shared.task;

import java.io.Serializable;

public class ActiveTask implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Task activeQuest;
	private ActiveTask nextQuest;
	
	public ActiveTask() {
	}
	
	public ActiveTask(Task q){
		this.activeQuest = q;
	}
	
	public ActiveTask(Task a, Task n){
		this.activeQuest = a;
		this.nextQuest = new ActiveTask(n);
	}
	
	public ActiveTask(Task a, ActiveTask n){
		this.activeQuest = a;
		this.nextQuest = n;
	}
	
	public void addQuest(Task q) {
		nextQuest = new ActiveTask(q);
	}
	
	public void addQuest(ActiveTask q) {
		nextQuest = q;
	}
	
	public boolean hasNextQuest(){
		return nextQuest != null;
	}
	
	public ActiveTask getNextQuest(){
		return nextQuest;
	}
	
	public String getTitle(){
		return activeQuest.getTitle();
	}
	
	public String getDesc() {
		return activeQuest.getDesc();
	}
	
	public int getDuration(){
		return activeQuest.getDuration();
	}
	
	public int getReward(){
		return activeQuest.getReward();
	}

}
