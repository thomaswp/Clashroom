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
	public BattleFactory getBattle(long id) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		BattleEntity entity = pm.getObjectById(BattleEntity.class, id);
		BattleFactory factory = entity.getBattleFactory();
		pm.close();
		return factory;
	}

	@Override
	public List<BattleFactory> getBattles() throws IllegalArgumentException {
		ArrayList<BattleFactory> factories = new ArrayList<BattleFactory>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<BattleEntity> entities = QueryUtils.query(pm, BattleEntity.class, "");
		for (BattleEntity entity : entities) {
			factories.add(entity.getBattleFactory());
		}
		pm.close();
		return factories;
	}

}
