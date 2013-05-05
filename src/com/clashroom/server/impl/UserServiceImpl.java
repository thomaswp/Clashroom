package com.clashroom.server.impl;

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
import com.clashroom.shared.entity.UserEntity;

import com.google.appengine.api.datastore.Entities;
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
}
