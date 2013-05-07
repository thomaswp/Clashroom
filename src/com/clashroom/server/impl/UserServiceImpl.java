package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.clashroom.client.services.UserInfoService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.NewsfeedEntity;
import com.clashroom.shared.entity.QuestEntity;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.news.NewsfeedItem;
import com.clashroom.shared.news.QuestNews;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet 
implements UserInfoService {

	public static UserEntity getCurrentUser(PersistenceManager pm) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user == null) return null;
		
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		return entity;
	}
	
	@Override
	public UserEntity getUser() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user == null) return null;

		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		
		if (entity == null) {
			entity = new UserEntity(user.getEmail());
//			pm.makePersistent(entity);
//			pm.flush();
		} else {
			entity = pm.detachCopy(entity);
		}
		
		pm.close();
		
		return entity;
	}
	
	@Override
	public List<UserEntity> getAllUsers() {
		LinkedList<UserEntity> entities = new LinkedList<UserEntity>();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<UserEntity> attachedEntities = QueryUtils.query(pm, UserEntity.class, "");
		
		for (UserEntity entity : attachedEntities) {
			entities.add(pm.detachCopy(entity));
		}
		
		return entities;
	}

	@Override
	public void setUser(UserEntity user) {
		//if (user.getId() == null) throw new RuntimeException("No id!");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (QueryUtils.query(pm, UserEntity.class, "email==%s", user.getEmail()).size() != 0) {
			Debug.write("User already exists");
			return;
		}
		
		DragonEntity dragon = user.getDragon();
		
		DragonClass dragonClass = DragonClass.getById(dragon.getDragonClassId());
		dragonClass.setUp(dragon);

		pm.makePersistent(user);
		pm.flush();
		pm.close();
	}

	@Override
	public void learnSkill(int id) {

		Skill skill = Skill.getById(id);
		if (skill == null) {
			throw new RuntimeException("No skill with id " + id);
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		UserEntity user = getCurrentUser(pm);
		if (user == null) {
			pm.close();
			throw new RuntimeException("User not logged in!");
		}
		
		if (user.getSkillPoints() < skill.getSkillPointCost()) {
			pm.close();
			throw new RuntimeException("Not enough skill points!");
		}
		if (user.getDragon().getSkills().contains(skill.getId())) {
			pm.close();
			throw new RuntimeException("Skill already learned!");
		}
		
		user.setSkillPoints(user.getSkillPoints() - skill.getSkillPointCost());
		DragonEntity dragon = pm.detachCopy(user.getDragon());
		dragon.getSkills().add(skill.getId());
		user.setDragon(dragon);
		pm.makePersistent(user);
		
		pm.flush();
		pm.close();
	}

	@Override
	public List<NewsfeedItem> getNews(List<Long> users, int count) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		List<NewsfeedEntity> entities = new ArrayList<NewsfeedEntity>();
		for (Long userId : users) {
			entities.addAll(QueryUtils.query(pm, NewsfeedEntity.class, "playerIds.contains(%s)", userId));
		}
		
		//TODO: Better implementation than just grabbing everything...
		Collections.sort(entities, new Comparator<NewsfeedEntity>() {
			@Override
			public int compare(NewsfeedEntity o1, NewsfeedEntity o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		List<NewsfeedItem> items = new ArrayList<NewsfeedItem>();
		for (int i = 0; i < count && i < entities.size(); i++) {
			items.add(entities.get(i).getItem());
		}
		
		pm.close();
		return items;
	}

	@Override
	public void addExp(int exp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		addExpImpl(pm, exp);
		pm.close();
	}
	
	public static void addExpImpl(PersistenceManager pm, int exp) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		Debug.write("Exp: %d", exp);
		
		if (user == null) return;
		
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		
		if (entity == null) {
			throw new RuntimeException("No UserEntity");
		}
		entity.getDragon().addExp(exp);
		entity.setDragon(pm.detachCopy(entity.getDragon()));
		
		pm.makePersistent(entity);
	}
	
	@Override 
	public void completeQuest(long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserEntity user = getUser();
			if (user == null) throw new RuntimeException("No user");
			
			QuestEntity quest = QueryUtils.queryUnique(pm, QuestEntity.class, "id==%s", id);
			if (quest == null) throw new RuntimeException("No quest with id :" + id);
			
			
			user.addCompletedQuest(id);
			user.setCompletedQuests(user.getCompletedQuests());
			for(Long itemID: quest.getItemsRewarded()){
				user.addItemToInventory(itemID);
			}
			user.setItemsIventory(user.getItemInventory());
			
			QuestNews qn = new QuestNews(new Date(), quest.getQuestName(), user.getUsername(), user.getId());
			NewsfeedEntity ne = new NewsfeedEntity(qn);
			pm.makePersistent(ne);
			
			pm.makePersistent(user);
		} finally {
			pm.close();
		}
		
	}
}
