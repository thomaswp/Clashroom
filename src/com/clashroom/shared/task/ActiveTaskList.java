package com.clashroom.shared.task;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import com.clashroom.shared.entity.ActiveBountyEntity;

public class ActiveTaskList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long start;
	private long aqStart;
	private LinkedList<ActiveTask> list;
	private LinkedList<ActiveTask> history;
	private Long eId;
	private Long userID;
	
	public ActiveTaskList() {
		list = new LinkedList<ActiveTask>();
		history = new LinkedList<ActiveTask>();
	}
	
	public ActiveTaskList(LinkedList<ActiveTask> list){
		this.list = list;
	}
	
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
	
	public long activeTimeLeft() {
		return aqStart + activeDuration() - new Date().getTime();
	}
	
	public int activeDuration(){
		return getActiveQuest().getDuration();
	}
	
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
