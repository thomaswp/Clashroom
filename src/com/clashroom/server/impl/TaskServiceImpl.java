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
	public ActiveTaskList getActiveQuests(Long userID) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ActiveBountyEntity abe = QueryUtils.queryUnique(pm, ActiveBountyEntity.class, "userID==%s", userID);
		if (abe == null){
			abe = new ActiveBountyEntity();
			abe.setUser(userID);
		}
		ActiveTaskList atl = new ActiveTaskList(abe);
		pm.close();
		return atl;
		
	}

	@Override
	public String persistAQL(Long userID, ActiveTaskList aql) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ActiveBountyEntity entity = new ActiveBountyEntity();
		if (aql.getId() != null) {
			entity = pm.getObjectById(ActiveBountyEntity.class, aql.getId());
		} else {
			entity.setUser(userID);
		}
		entity.setActiveQuests(aql);
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
		return "AQL persisted";
	}
	
	@Override
	public String completeQuest(Long userID, ActiveTaskList atl) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserServiceImpl.addExpImpl(pm, atl.getActiveQuest().getReward());
		atl.removeFirst();
		
		ActiveBountyEntity entity = new ActiveBountyEntity();
		entity = pm.getObjectById(ActiveBountyEntity.class, atl.getId());
		entity.setActiveQuests(atl);
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
		return "Bounty completed";
	}
	


}
