package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.clashroom.client.services.TaskService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.entity.ActiveBountyEntity;
import com.clashroom.shared.entity.NewsfeedEntity;
import com.clashroom.shared.entity.TaskEntity;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.news.BattleNews;
import com.clashroom.shared.news.TaskNews;
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
	public String persistAQL(Long userID, ActiveTaskList atl) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//ActiveBountyEntity entity = new ActiveBountyEntity();
		
		ActiveBountyEntity abe = QueryUtils.queryUnique(pm, ActiveBountyEntity.class, "userID==%s", userID);
		if (abe == null){
			abe = new ActiveBountyEntity();
			abe.setUser(userID);
		}
		abe.setActiveQuests(atl);
		try {
			pm.makePersistent(abe);
		} finally {
			pm.close();
		}
		return "AQL persisted";
	}
	
	@Override
	public String completeQuest(Long userID, ActiveTaskList atl) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserInfoServiceImpl.addExpImpl(pm, atl.getActiveQuest().getReward());
		
		UserEntity user = QueryUtils.queryUnique(pm, UserEntity.class, "id==%s", userID);
		if (user != null) {
			NewsfeedEntity item = new NewsfeedEntity(new TaskNews(atl.getActiveQuest(), userID, user.getUsername()));
			pm.makePersistent(item);
		}
		
		atl.removeFirst();
		
		//ActiveBountyEntity entity = new ActiveBountyEntity();
		ActiveBountyEntity abe = QueryUtils.queryUnique(pm, ActiveBountyEntity.class, "userID==%s", userID);
		abe.setActiveQuests(atl);
		try {
			pm.makePersistent(abe);
			pm.flush();
		} finally {
			pm.close();
		}
		return "Bounty completed";
	}
	


}
