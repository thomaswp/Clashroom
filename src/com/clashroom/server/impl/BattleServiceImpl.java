package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.clashroom.client.services.BattleService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BattleServiceImpl extends RemoteServiceServlet implements BattleService {
	private static final long serialVersionUID = 1L;

	@Override
	public BattleEntity getBattle(long id) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		BattleEntity entity = pm.getObjectById(BattleEntity.class, id);
		entity.getBattleFactory();
		entity = pm.detachCopy(entity);
		pm.close();
		return entity;
	}

	@Override
	public List<BattleEntity> getBattles() throws IllegalArgumentException {
		ArrayList<BattleEntity> entities = new ArrayList<BattleEntity>();
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) {
			return entities;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity userEntity = QueryUtils.queryUnique(pm, UserEntity.class, "email==%s", user.getEmail());
		
		List<BattleEntity> queryEntities = QueryUtils.query(
				pm, BattleEntity.class, "playerIds.contains(%s)", userEntity.getId());
		for (BattleEntity entity : queryEntities) {
			entity.getBattleFactory();
			entities.add(pm.detachCopy(entity));
		}
		pm.close();
		return entities;
	}
}
