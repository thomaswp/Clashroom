package com.clashroom.server;

import javax.jdo.PersistenceManager;

import com.clashroom.client.services.UserInfoService;
import com.clashroom.shared.Debug;
import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.data.UserEntity;
import com.clashroom.shared.dragons.DragonClass;

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
			pm.makePersistent(entity);
		}
		
		entity = pm.detachCopy(entity);
		
		pm.close();
		
		return entity;
	}

	@Override
	public void setUser(UserEntity user, DragonEntity dragon) {
		DragonClass dragonClass = DragonClass.getById(dragon.getDragonClassId());
		dragonClass.setUp(dragon);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.currentTransaction().begin();
		pm.makePersistent(dragon);
		pm.currentTransaction().commit();
		
		pm.currentTransaction().begin();
		Debug.write("Dragon id: %d", dragon.getId());
		user.setDragonId(dragon.getId());
		pm.makePersistent(user);
		pm.currentTransaction().commit();
		pm.close();
	}

}
