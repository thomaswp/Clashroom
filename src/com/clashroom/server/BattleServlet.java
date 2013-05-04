package com.clashroom.server;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.server.impl.BattleServiceImpl;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.actions.ActionExp;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.battlers.DragonBattler;
import com.clashroom.shared.battle.battlers.GoblinBattler;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.QueuedBattleEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.appengine.api.datastore.AdminDatastoreService.QueryBuilder;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class BattleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Date date = new Date();
		List<QueuedBattleEntity> queuedBattles = QueryUtils.query(pm, QueuedBattleEntity.class, "time < %s", date);
		for (QueuedBattleEntity qb : queuedBattles) {
			BattleServiceImpl.createBattleImpl(pm, qb);
			pm.deletePersistent(qb);
		}
		Debug.write("Created %d battles", queuedBattles.size());
		pm.close();
	}

	
}
