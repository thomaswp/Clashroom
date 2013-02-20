package com.clashroom.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.server.data.PlayerEntity;
import com.clashroom.server.data.TestEntity;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		resp.getWriter().println("<b>Hello</b>!!!");
		
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<PlayerEntity> entities = QueryUtils.query(PlayerEntity.class, "id > %s && name==%s", -1L, "Thomas");
		for (PlayerEntity e : entities) {
			resp.getWriter().println(String.format("%s (%s)", e.getName(), e.getDragon()));
		}
		
		String name = req.getParameter("name");
		if (name != null) {
			PlayerEntity entity = new PlayerEntity(name, name + "'s Dragon");
			pm.makePersistent(entity);
		}
		
		pm.close();
	}

}
