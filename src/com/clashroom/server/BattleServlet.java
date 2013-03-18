package com.clashroom.server;

import java.io.IOException;
import java.util.LinkedList;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.Debug;
import com.clashroom.shared.RandomTest;
import com.clashroom.shared.actions.ActionFinish;
import com.clashroom.shared.actions.BattleAction;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.battlers.FairyBattler;
import com.clashroom.shared.battlers.GoblinBattler;
import com.clashroom.shared.data.BattleEntity;

public class BattleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		LinkedList<Battler> teamA = new LinkedList<Battler>();
		LinkedList<Battler> teamB = new LinkedList<Battler>();
		teamA.add(new GoblinBattler(10));
		teamA.add(new GoblinBattler(13));
		teamA.add(new FairyBattler(12));
		teamB.add(new GoblinBattler(15));
		teamB.add(new GoblinBattler(12));
		teamB.add(new FairyBattler(8));
		BattleEntity battle = new BattleEntity(new BattleFactory(
				"Lefties", teamA, "Righties", teamB));
		pm.makePersistent(battle);

		
//		BattleEntity battle = QueryUtils.queryUnique(pm, BattleEntity.class, "id==%s", 27L);
//		
//		Battle b = battle.getBattleFactory().generateBattle();
//		BattleAction action = b.nextAction();
//		while (!(action instanceof ActionFinish)) {
//			action = b.nextAction();
//		}
//		Debug.write(((ActionFinish) action).teamAVictor);
		
		pm.close();
	}

	
}
