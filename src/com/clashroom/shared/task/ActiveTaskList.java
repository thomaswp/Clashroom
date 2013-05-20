package com.clashroom.shared.task;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import com.clashroom.shared.entity.ActiveBountyEntity;

/**
 * A class the manages a player's list of {@link ActiveTask}
 * 
 * @author deagle
 *
 */
public class ActiveTaskList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long start;
	private long aqStart;
	private LinkedList<ActiveTask> list;
	private LinkedList<ActiveTask> history;
	private Long eId;
	private Long userID;
	
	/**
	 * Constructs a blank ActiveTaskList
	 */
	public ActiveTaskList() {
		list = new LinkedList<ActiveTask>();
		history = new LinkedList<ActiveTask>();
	}
	
	/**
	 * Constructs an ActiveTaskList given a LinkedList of {@link ActiveTask}
	 * @param list the LinkedList of {@link ActiveTask}
	 */
	public ActiveTaskList(LinkedList<ActiveTask> list){
		this.list = list;
	}
	
	/**
	 * Constructs an ActiveTaskList from a given {@link ActiveBountyEntity}
	 * @param entity the {@link ActiveBountyEntity} to fetch data from
	 */
	public ActiveTaskList(ActiveBountyEntity entity){
		list = entity.getActiveQuests().getList();
		history = entity.getActiveQuests().getHistory();
		aqStart = entity.getActiveQuests().getAqStart();
		start = entity.getActiveQuests().getStart();
		eId = entity.getId();
		userID = entity.getUserId();
	}
	
	public boolean isEmpty(){
		return list.isEmpty();
	}
	
	/**
	 * Adds a given {@link Task} to the list and initializes
	 * the start time and history if needed
	 * @param q the Task to add
	 */
	public void addQuest(Task q){
		if (isEmpty()){
			start = new Date().getTime();
			aqStart = start;
			history = new LinkedList<ActiveTask>();
		}
		list.add(new ActiveTask(q));
		history.add(list.getLast());
	}
	
	public ActiveTask getActiveQuest(){
		return list.getFirst();
	}
	
	/**
	 * Sums up the duration of all of the quests in the current quest history
	 * @return total duration
	 */
	public int getTotalDuration(){
		int duration = 0;
		for (ActiveTask q : history){
			duration += q.getDuration();
		}
		return duration;
	}
	
	public long getStartTime(){
		return start;
	}
	
	/**
	 * Calculates the remaining time left for the current active bounty
	 * @return current bounty duration
	 */
	public long activeTimeLeft() {
		return aqStart + activeDuration() - new Date().getTime();
	}
	
	public int activeDuration(){
		return getActiveQuest().getDuration();
	}
	
	/**
	 * Calculates the remaining time left for the entire list of bounties
	 * @return bounty list duration
	 */
	public long totalTimeLeft() {
		return start + getTotalDuration() - new Date().getTime();
	}
	
	public long activeQuestStart(){
		return aqStart;
	}
	
	public LinkedList<ActiveTask> getAllQuests(){
		return list;
	}
	
	public void completeQuest(){
		
//		Debug.write("Doin ma callbacks");
//		userInfoService.addExp(getActiveQuest().getReward(), new AsyncCallback<Void>(){
//
//			@Override
//			public void onSuccess(Void result) {
//				Debug.write(getActiveQuest());
//				System.out.println("Recieved " + getActiveQuest().getReward() + " experience points!");
//				list.removeFirst();
//			}
//
//			@Override
//			public void onFailure(Throwable caught) {
//				System.out.println("Dat shit is broke");
//			}
//			
//		});
		aqStart = new Date().getTime();
	}
	
	public void removeFirst() {
		list.removeFirst();
		aqStart = new Date().getTime();
	}
	
	public void removeQuest(int i){
		history.remove(list.get(i));
		list.remove(i);
		if (i == 0){
			start = new Date().getTime();
			aqStart = start;
		}
	}

	public long getStart() {
		return start;
	}

	public long getAqStart() {
		return aqStart;
	}

	public LinkedList<ActiveTask> getList() {
		return list;
	}

	public LinkedList<ActiveTask> getHistory() {
		return history;
	}
	
	public Long getId(){
		return eId;
	}
	
	public void setId(Long id) {
		eId = id;
	}
	
	public Long getUserID(){
		return userID;
	}
	
	public void setUserID(Long id){
		userID = id;
	}

}
