package com.clashroom.server;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.server.impl.BattleServiceImpl;
import com.clashroom.shared.Debug;
import com.clashroom.shared.entity.QueuedBattleEntity;

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
		pm.flush();
		pm.close();
	}

	
}
