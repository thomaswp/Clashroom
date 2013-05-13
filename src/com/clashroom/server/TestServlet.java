package com.clashroom.server;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.shared.entity.ActiveBountyEntity;
import com.clashroom.shared.entity.TaskEntity;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.task.ActiveTaskList;
import com.clashroom.shared.task.Task;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class TestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		String d = req.getParameter("do");
		if ("task".equals(d)) {
			createTestQuests(resp);
			resp.getWriter().println(createQueue());
		} else if ("sp".equals(d)) {
			addSp();
			resp.getWriter().println("+1 sp");
		} else if ("quests".equals(d)) {
			
		} else {
			resp.getWriter().println("use ?do=something");
		}
	}
	
	public void addSp() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user == null) return;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		entity.setSkillPoints(entity.getSkillPoints() + 1);
		
		pm.makePersistent(entity);
		pm.close();
	}
	
	public String createQueue(){
		ActiveTaskList aql = new ActiveTaskList();
		aql.addQuest(new Task("The Dragon Oracle", "High above the city of Khost is a dragon’s cave, where the monster will answer questions for a price. Only the bravest of Warriors can hope to reach the dragon and learn their answer, for he keeps the dungeon well stocked with creatures. The Warriors have been hired to reach the dragon and ask a single question: is the treaty offered by a nearby count trustworthy or not? The Duke hiring the Warriors pays well, an objective room treasure and 50 gold each.", 2*60*60*1000, 50000));
		aql.addQuest(new Task("The Skaven Priests of Torne", "Princess Janitta, the daughter of King Reinwelle, has been captured by the evil skavens that control the sewers beneath the ancient city of Torne. The warriors have been commissioned to rescue her before she is sacrificed by the plague priests in their temple hidden somewhere deep within the meandering sewer passageways. ", 240000, 120));
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ActiveBountyEntity entity = new ActiveBountyEntity(aql);
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
		return "List created";
	}
	
	public String createQuest(Task q){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TaskEntity entity = new TaskEntity(q);
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
		return q.getTitle() + " added to datastore";
	}
	
	public void createTestQuests(HttpServletResponse resp) throws ServletException, IOException {
		resp.getWriter().println(createQuest(new Task(
				"The Dragon of the Lethesand Desert",
				"An angry noblewoman named Elient seeks a dragon trainer to expose a corrupt aristocrat who serves the Dragon of the Lethesand Desert.",
				2*60*60*1000, 4)));
		resp.getWriter().println(createQuest(new Task(
				"The Lost City of Cabrook",
				"A charming aristocrat named Ambes seeks a dragon trainer to recover and destroy an evil artifact from the lost city of Cabrook in the Jungle of Jade.",
				1*60*60*1000, 2)));
		resp.getWriter().println(createQuest(new Task(
				"The Ruins of Windselw Castle",
				"A guarded noblewoman named Hansa seeks a dragon trainer to recover and destroy an evil artifact from the ruins of Windselw Castle.",
				2*60*60*1000, 4)));
		resp.getWriter().println(createQuest(new Task(
				"The Assassins of Adras the Crimson",
				"A frantic priest named Monesto seeks a dragon trainer to protect him from the assassins of Adras the Crimson",
				3*60*60*1000, 6)));
		resp.getWriter().println(createQuest(new Task(
				"The Demon of the Huamana Jungle",
				"An ex-adventurer named Bedge seeks a dragon trainer to thwart the monstrous plan of the Demon of the Huamana Jungle.",
				2*60*60*1000, 4)));
		resp.getWriter().println(createQuest(new Task(
				"Smoore Stronghold",
				"A merchant named Thurey seeks a dragon trainer to recover and destroy an evil artifact from Smoore Stronghold.",
				1*60*60*1000, 2)));
		resp.getWriter().println(createQuest(new Task(
				"The Crystal Desert",
				"A charming noblewoman named Andis seeks a dragon trainer to investigate bizarre monsters which wander the Crystal Desert.",
				2*60*60*1000, 4)));
		resp.getWriter().println(createQuest(new Task(
				"The lost city of Runivik",
				"A priest named Hippusarp seeks a dragon trainer to recover and destroy an evil artifact from the lost city of Runivik in the Jungle of Spears.",
				3*60*60*1000, 6)));
		resp.getWriter().println(createQuest(new Task(
				"The Behemoth of the Tuli March",
				"A guarded ex-adventurer named Nieleon Weke seeks a dragon trainer to slay the Behemoth of the Tuli March and retrieve its hide.",
				2*60*60*1000, 4)));
		resp.getWriter().println(createQuest(new Task(
				"The Pirates of the Silent Sea",
				"A merchant named Ether seeks a dragon trainer to recover a caravan of exotic goods from the pirates of the Silent Sea.",
				1*60*60*1000, 2)));
	}

}
