package com.clashroom.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.clashroom.client.BattleService;
import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.Debug;
import com.clashroom.shared.data.BattleEntity;
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
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<BattleEntity> queryEntities = QueryUtils.query(pm, BattleEntity.class, "");
		for (BattleEntity entity : queryEntities) {
			entity.getBattleFactory();
			entities.add(pm.detachCopy(entity));
		}
		pm.close();
		return entities;
	}

}
