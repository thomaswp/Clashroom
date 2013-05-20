package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.shared.task.ActiveTaskList;
import com.clashroom.shared.task.Task;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("quest")
public interface TaskService extends RemoteService {
	
	//Requests datastore for list of available quests
	ArrayList<Task> getAvailableQuests() throws IllegalArgumentException;
	
	//Requests datastore for the list of active quests
	ActiveTaskList getActiveQuests(Long userID) throws IllegalArgumentException;
	
	//Requests datastore to persist the given active quest list
	String persistAQL(Long userID, ActiveTaskList aql) throws IllegalArgumentException;
	
	//Completes a task, dishes out the reward then persists the active bounty list
	String completeQuest(Long userID, ActiveTaskList atl) throws IllegalArgumentException;
}
