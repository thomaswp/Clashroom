package com.clashroom.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.clashroom.shared.battle.skills.AttackSkill;
import com.clashroom.shared.battle.skills.FireBreathSkill;
import com.clashroom.shared.battle.skills.FireballSkill;
import com.clashroom.shared.battle.skills.HealSkill;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.ActiveBountyEntity;
import com.clashroom.shared.entity.ItemEntity;
import com.clashroom.shared.entity.ItemEntity.ItemType;
import com.clashroom.shared.entity.QuestEntity;
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
			createRealWorldQuests();
			resp.getWriter().println("10 Quests have been added");
		} else if ("items".equals(d)){
			createItems();
			resp.getWriter().println("Items have been added");
		} else if ("setup".equals(d)) { 
			createTestQuests(resp);
			createItems();
			createRealWorldQuests();
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

public void createRealWorldQuests(){
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<QuestEntity> questList = new ArrayList<QuestEntity>();
		List<Long> itemList = new ArrayList<Long>();
		
		if(getItems() !=null){
			for(ItemEntity item : getItems()){
				itemList.add(item.getId());
			}
		}
		
		QuestEntity quest1 = new QuestEntity("A Dragon Trainer is Born", "You are now a dragon trainer, but what do you do? How do you become the best trainer out there? " +
				"Your adventure leads you to the temple of Belk. There you feel inclined to sit in the back on the first floor and begin your studies." +
				" You start by checking a very large Webster dictionary to see what more you can learn about dragons. You find this dictionary closest to the window and" +
				"on top of a book shelf.", "dragonknowledge",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "Congratulations! You are well on your way to becoming a great dragon trainer!","",
                itemList, 1);
		questList.add(quest1);
		
		QuestEntity quest2 = new QuestEntity("Wait..what do dragons eat again?", "You are still in the process of understanding your role as a dragon trainer. " +
				"Dragons, like other animals, need food but what do dragons eat? And where do you go to find it? Luckily you over heard another dragon trainer mention something " +
				"about going to the Mosley market to pick up some dragon food for the road. You decide to travel to Mosley market to find some food for your dragon" +
				" no matter what. Even if that means looking under every table.", "dragonchow",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "Delicious! Is what your dragon would say if it could talk... but you know deep inside that it enjoyed the food you brought it.","",
                itemList, 1);
		questList.add(quest2);
		
		QuestEntity quest3 = new QuestEntity("Dragon Trainer's License: Part 1", "Hmmmm, who would have thought you needed a license to train a dragon? Obtaining a " +
				" license shouldn't be too hard though, right? You decide to head to the castle of mathematicians and futuristic scientists who have the ability" +
				" to program these weird machines that look like boxes of light. What was the name of that castle again? You decide to sit and ponder the name" +
				" before you venture out for a license. Knowing the name of the castle first will make it easier to find.", "Duke",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "It seems like you're at the right place. As you walk in, you take a right and notice a room of futuristic scientist hard at work on their weird light boxes.","",
                itemList, 1);
		questList.add(quest3);
		
		QuestEntity quest4 = new QuestEntity("Dragon Trainer's License: Part 2", " After walking past the room with light boxes you notice a seating area." +
				" You figure if you just wait here someone will assist you momentarily and give you a dragon trainer's license. To past the time you" +
				" look through some left out scrolls on the table. What's this? You see a letter stating that those who wish to obtain a dragon trainer license must" +
				" speak to a sorceress by the name of...without second thought you write down the name and go to seek out this person.", "Haldas Lunn Von",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "Haldas Lunn Von, hmmm you have never heard of her. And there is no record of her ever existing in the Land of Elon; you take a closer look at the name.","",
                itemList, 1);
		questList.add(quest4);
		
		QuestEntity quest5 = new QuestEntity("Dragon Trainer's License: Part 3", " Who is this sorceress? At this rate you'll never be able to get a License." +
				" Perhaps if you go back to the castle where you found this letter you will be able to find her. Checking the name tags on the outside of" +
				" the room doors seems like a good idea. You look at the name one more time...hmmm is this possibly an anagram of some sort?", "Shannon Duvall",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "Yes! You have figured it out. Haldas Lunn Von turns out to be an anagram for Shannon Duvall! She will be able to give you a dragon trainer's license!","",
                itemList, 1);
		questList.add(quest5);
		
		QuestEntity quest6 = new QuestEntity("Dragon Trainer's License: Part 4", " You finally find the sorceress who will provide you with a dragon trainer's license." +
				" She is reluctant to give you the license, but after some debate she decides to give you a license if and only if you can answer a question for her." +
				" What was the first light box (computer) bug? You respond with an answer, hopefully you're right or you won't be able to get your dragon trainer's license.", "zuztye",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "Awesome, a moth was the correct answer. You have finally received your dragon trainer's license! Look out dragon trainers there is a new kid in town... ","",
                itemList, 1);
		questList.add(quest6);
		
		QuestEntity quest7 = new QuestEntity("Wealth flows in the Land: Part 1"," While exploring The Land of Oaks you notice a change." +
				" The castle formally named castle A has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advantageous" +
				" to know the wealthy in this land. Monarchs are fans of dragon tournaments and often sponsor young trainers they like.", "Williams",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 1);
		questList.add(quest7);
		
		QuestEntity quest8 = new QuestEntity("Wealth flows in the Land: Part 2"," While continuing to explore The Land of Oaks you notice another change." +
				" The castle formally named castle E has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advantageous" +
				" to know the wealthy in this land. Monarchs are fans of dragon tournaments and often sponsor young trainers they like.", "Council",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                " You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 1);
		questList.add(quest8);
		
		QuestEntity quest9 = new QuestEntity("Wealth flows in the Land: Part 3"," While continuing to explore The Land of Oaks you notice another change." +
				" The castle formally named castle E has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advantageous" +
				" to know the wealthy in this land. Monarchs are fans of dragon tournaments and often sponsor young trainers they like.", "Brown",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 1);
		questList.add(quest9);
		
		QuestEntity quest10 = new QuestEntity("Wealth flows in the Land: Part 4"," While continuing to explore The Land of Oaks you notice another change." +
				" The castle formally named castle F has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advantageous" +
				" to know the wealthy in this land. Monarchs are fans of dragon tournaments and often sponsor young trainers they know and like.", "Sullivan",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 1);
		questList.add(quest10);
    	 
         try {
             pm.makePersistentAll(questList);
         } finally {
             pm.close();
         }
		
	}
	
	public void createItems(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		List<ItemEntity> itemList = new ArrayList<ItemEntity>();
		Skill fireBall = new FireballSkill();
		Skill fireBreath = new FireBreathSkill();
		Skill heal = new HealSkill();
		Skill attack = new AttackSkill();
    	
    	ItemEntity fireBallScroll = new ItemEntity("Fire Ball Scroll","Reading this scroll will" +
    			" cause your dragon to release a fire ball attack",fireBall,ItemType.ACTIVE,"ScrollImage.png");
    	
    	ItemEntity fireBreathScroll = new ItemEntity("Fire Breath Scroll","Reading this scroll will have your dragon release" +
    			" a fire breath attack",fireBreath,ItemType.PASSIVE,"ScrollImage.png");
    	
    	ItemEntity healScroll = new ItemEntity("Scroll of Healing","Reading this scroll will" +
    			" heal your dragon",heal,ItemType.PASSIVE,"ScrollImage.png");
    	
    	ItemEntity sneakAttack = new ItemEntity("Invisibily Amulet","Using this amulet" +
    			" will allow your dragon to pull off a stealthy sneak attack",attack,ItemType.ACTIVE,"ScrollImage.png");
    	
    	itemList.add(fireBallScroll);
    	itemList.add(fireBreathScroll);
    	itemList.add(healScroll);
    	itemList.add(sneakAttack);
		
		try {
            pm.makePersistentAll(itemList);
        } finally {
            pm.close();
        }
		
	}
	
	public ArrayList<ItemEntity> getItems(){
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Collection<ItemEntity> itemCollection = null;
		ArrayList<ItemEntity> itemList = new ArrayList<ItemEntity>();
		
		try{
			Query q = pm.newQuery("select from " + ItemEntity.class.getName());
			List<ItemEntity> results = (List<ItemEntity>) q.execute();
			itemCollection = pm.detachCopyAll(results);
			
			for(ItemEntity iE : itemCollection){
				itemList.add(iE);
			}
			
		}finally{
			pm.close();
		}
		return itemList;
	}
	
}
