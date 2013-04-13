package com.clashroom.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceAware;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.battlers.DragonBattler;
import com.clashroom.shared.battlers.GoblinBattler;
import com.clashroom.shared.data.BattleEntity;
import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.data.TestEntitySub;
import com.clashroom.shared.data.UserEntity;
import com.clashroom.shared.data.TestEntity;
import com.clashroom.shared.dragons.LionDragon;
import com.google.appengine.api.users.UserService;
import com.google.gwt.user.client.Random;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		
		resp.setContentType("text/html");
		resp.getWriter().println("<b>Hello</b>!!!<br />");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
//		UserEntity userEntity = QueryUtils.queryUnique(pm, UserEntity.class, "email==%s", "test@example.com");
		
//		LinkedList<Battler> teamA = new LinkedList<Battler>(), teamB = new LinkedList<Battler>();
//		DragonBattler db = new DragonBattler(userEntity.getDragon(), userEntity.getId());
//		
//		teamA.add(db);		
//		teamB.add(new GoblinBattler(1));
//		teamB.add(new GoblinBattler(1));
//		
//		BattleFactory factory = new BattleFactory("Rufus", teamA, "Goblins", teamB);
//		BattleEntity battleEntity = new BattleEntity(factory);
//		
//		int exp = 0;
//		for (Battler battler : teamB) {
//			exp += battler.getExpReward();
//		}
//		if (!battleEntity.isTeamAVictor()) {
//			exp *= BattleEntity.LOSE_EXP_FACTOR;
//		}
//		Debug.write(exp);
//		DragonEntity dragon = userEntity.getDragon();
//		dragon.addExp(exp);
//		dragon.setExperience(dragon.getExperience());
//		userEntity.setDragon(dragon);
//		Debug.write(userEntity.getDragon().getExperience());
//		
//		pm.makePersistent(battleEntity);
		
//		pm.makePersistent(userEntity);
		
		TestEntity test = pm.getObjectById(TestEntity.class, 75L);
		test.getSub().setState("New State");
		
		pm.makePersistent(test);
		
		pm.close();
		
		//resp.getWriter().println("" + battleEntity.getId());
	}

}
