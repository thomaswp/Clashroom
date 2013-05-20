package com.clashroom.shared.task;

import java.io.Serializable;

/**
 * Represents a Task that is in play and is owned by a player
 * @author deagle
 *
 */
public class ActiveTask implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Task activeQuest;
	private ActiveTask nextQuest;
	
	public ActiveTask() {
	}
	
	/**
	 * Constructs an ActiveTask from a given {@link Task}
	 * @param q the Task to fetch data from
	 */
	public ActiveTask(Task q){
		this.activeQuest = q;
	}
	
	/**
	 * Constructs an ActiveTask with a current and next {@link Task}
	 * @param a the Task to make active
	 * @param n the Task to queue up
	 */
	public ActiveTask(Task a, Task n){
		this.activeQuest = a;
		this.nextQuest = new ActiveTask(n);
	}
	
	/**
	 * Constructs an ActiveTask with a current {@link Task} and
	 * next ActiveTask
	 * @param a the Task to make active
	 * @param n the ActiveTask to queue up
	 */
	public ActiveTask(Task a, ActiveTask n){
		this.activeQuest = a;
		this.nextQuest = n;
	}
	
	/**
	 * Sets nextQuest to the given {@link Task}
	 * @param q the Task to fetch data from
	 */
	public void addQuest(Task q) {
		nextQuest = new ActiveTask(q);
	}
	
	/**
	 * Sets nextQueue to the given ActiveTask
	 * @param q the ActiveTask to fetch data from
	 */
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
