package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.shared.task.ActiveTaskList;
import com.clashroom.shared.task.Task;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TaskServiceAsync {
	void getAvailableQuests(AsyncCallback<ArrayList<Task>> callback) throws IllegalArgumentException;
	void getActiveQuests(Long userID, AsyncCallback<ActiveTaskList> callback) throws IllegalArgumentException;
	void persistAQL(Long userID, ActiveTaskList aql, AsyncCallback<String> callback) throws IllegalArgumentException;
}
