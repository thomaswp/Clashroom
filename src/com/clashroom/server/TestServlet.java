package com.clashroom.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.server.data.TestEntity;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		resp.getWriter().println("<b>Hello</b>!!!");
		

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q  = pm.newQuery(TestEntity.class);
		List<TestEntity> entities = (List<TestEntity>) q.execute();
		for (TestEntity e : entities) {
			resp.getWriter().println(e.getName());
		}
		
		String name = req.getParameter("name");
		if (name != null) {
			TestEntity entity = new TestEntity();
			entity.setName(name);
			pm.makePersistent(name);
		}
		
		pm.close();
	}

}
