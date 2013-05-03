package com.clashroom.server.impl;

import javax.jdo.PersistenceManager;

import com.clashroom.client.services.UserInfoService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.UserEntity;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet 
implements UserInfoService {

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
	public void setUser(UserEntity user) {
		//if (user.getId() == null) throw new RuntimeException("No id!");
		
		DragonEntity dragon = user.getDragon();
		
		DragonClass dragonClass = DragonClass.getById(dragon.getDragonClassId());
		dragonClass.setUp(dragon);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(user);
		pm.flush();
		pm.close();
	}

	@Override
	public void addExp(int exp) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		Debug.write("Exp: %d", exp);
		
		if (user == null) return;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		
		if (entity == null) {
			throw new RuntimeException("No UserEntity");
		}
		entity.getDragon().addExp(exp);
		entity.setDragon(pm.detachCopy(entity.getDragon()));
		
		pm.makePersistent(entity);
	}
}
