package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.clashroom.client.services.TaskService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.entity.ActiveBountyEntity;
import com.clashroom.shared.entity.TaskEntity;
import com.clashroom.shared.task.ActiveTaskList;
import com.clashroom.shared.task.Task;

public class TaskServiceImpl extends RemoteServiceServlet implements TaskService {
	private static final long serialVersionUID = 1L;

	@Override
	public ArrayList<Task> getAvailableQuests() throws IllegalArgumentException {
		List<TaskEntity> entities = QueryUtils.query(TaskEntity.class, "");
		ArrayList<Task> quests = new ArrayList<Task>();
		for (TaskEntity e : entities){
			quests.add(new Task(e));
		}
		return quests;
		
	}
	
	@Override
	public ActiveTaskList getActiveQuests() throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<ActiveBountyEntity> entities = QueryUtils.query(pm, ActiveBountyEntity.class, "");
		ActiveTaskList aql = new ActiveTaskList();
		for (ActiveBountyEntity e : entities){
			aql = new ActiveTaskList(e);
		}
		pm.close();
		return aql;
		
	}

	@Override
	public String persistAQL(ActiveTaskList aql) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ActiveBountyEntity entity = new ActiveBountyEntity();
		if (aql.getId() != null) {
			entity = pm.getObjectById(ActiveBountyEntity.class, aql.getId());
		}
		entity.setActiveQuests(aql);
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
		return "AQL persisted";
	}


}
