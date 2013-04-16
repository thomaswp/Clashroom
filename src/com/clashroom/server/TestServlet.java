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
import com.clashroom.shared.Formatter;
import com.clashroom.shared.actions.ActionExp;
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
		
		
	}

}
