package com.clashroom.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceAware;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.shared.Battle;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.battlers.GoblinBattler;
import com.clashroom.shared.data.BattleEntity;
import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.data.PlayerEntity;
import com.clashroom.shared.data.TestEntity;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		resp.getWriter().println("<b>Hello</b>!!!");
		
//		Battle battle = new Battle(new GoblinBattler(10), new GoblinBattler(15));
//		while (!battle.isOver()) {
//			System.out.println(battle.nextAction().toBattleString());
//			System.out.println(battle.getStatus());
//		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
//		List<BattleEntity> entities = QueryUtils.query(pm, BattleEntity.class, "");
//		for (BattleEntity e : entities) {
//			resp.getWriter().println(e.getBattler());
//		}
		
		String name = req.getParameter("name");
		if (name != null) {
//			Battler b = new Battler();
//			b.name = name;
//			BattleEntity be = new BattleEntity(b);
//			pm.makePersistent(be);
		}
		
		pm.close();
	}

}
