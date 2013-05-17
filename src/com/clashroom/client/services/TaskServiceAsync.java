package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.server.impl.TaskServiceImpl;
import com.clashroom.shared.task.ActiveTaskList;
import com.clashroom.shared.task.Task;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * See {@link TaskServiceImpl}
 */
public interface TaskServiceAsync {
	/** See {@link TaskServiceImpl#getAvailableQuests()} */
	void getAvailableQuests(AsyncCallback<ArrayList<Task>> callback) throws IllegalArgumentException;
	/** See {@link TaskServiceImpl#getActiveQuests(Long)} */
	void getActiveQuests(Long userID, AsyncCallback<ActiveTaskList> callback) throws IllegalArgumentException;
	/** See {@link TaskServiceImpl#persistAQL(Long, ActiveTaskList)} */
	void persistAQL(Long userID, ActiveTaskList aql, AsyncCallback<String> callback) throws IllegalArgumentException;
	/** See {@link TaskServiceImpl#completeQuest(Long, ActiveTaskList)} */
	void completeQuest(Long userID, ActiveTaskList atl, AsyncCallback<String> callback) throws IllegalArgumentException;
}
