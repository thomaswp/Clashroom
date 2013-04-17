package com.clashroom.server;

import java.io.IOException;
import java.util.LinkedList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.actions.ActionExp;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.battlers.DragonBattler;
import com.clashroom.shared.battle.battlers.GoblinBattler;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class BattleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) {
			resp.getWriter().println("No user");
			return;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity userEntity = QueryUtils.queryUnique(pm, UserEntity.class, "email==%s", user.getEmail());
		
		LinkedList<Battler> teamA = new LinkedList<Battler>(), teamB = new LinkedList<Battler>();
		DragonBattler db = new DragonBattler(userEntity.getDragon(), userEntity.getId());
		
		teamA.add(db);		
		int levelDown = db.level - 1;
		teamB.add(new GoblinBattler(db.level));
		if (levelDown > 0) {
			teamB.add(new GoblinBattler(levelDown));
		}
		
		BattleFactory factory = new BattleFactory(Formatter.format("%s", db.name, db.level), 
				teamA, "Goblins", teamB);
		BattleEntity battleEntity = new BattleEntity(factory);
		
		int exp = battleEntity.getTeamAExp();
		DragonEntity dragon = userEntity.getDragon();
		dragon = pm.detachCopy(dragon);
		dragon.addExp(exp);
		userEntity.setDragon(dragon);
		factory.addPostBattleAction(new ActionExp(db, exp, dragon.getLevel()));
		
		pm.makePersistent(battleEntity);
		pm.makePersistent(userEntity);
		pm.flush();
		pm.close();
		
		resp.getWriter().println("Success!");
	}

	
}
