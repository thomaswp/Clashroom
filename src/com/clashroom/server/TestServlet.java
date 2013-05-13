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
import com.clashroom.shared.entity.QuestEntity;
import com.clashroom.shared.entity.TaskEntity;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.entity.ItemEntity.ItemType;
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
		resp.getWriter().println(createQuest(new Task("The Forge of Karak Khazad", "Deep under the World’s Edge Mountains was a Dwarven stronghold of great wealth and reknown. Most of the world knew of Karak Khazad, and its fabulous metal works, producing items of power and beauty. But with the fall of the Dwarves, the forge at Karak Khazad was lost, and its enchanted anvil abandoned. Now the foul creatures living in the ruins use the anvil for their own purposes, and although unable to attain its full potential, the results are truly troubling nonetheless. The Dwarven Enchanter Smith Dol Gondur has hired the Warriors to retrieve this anvil, and return it to him. In payment, he will either offer D6x200 gold or an item he creates with the anvil. If the item is chosen, make two rolls on the Weapons and Armor Dungeon Room Treasure table, and choose the one the Warriors like the best. This he gives to them for free.", 12000, 60)));
		resp.getWriter().println(createQuest(new Task("Curse of the Wolf", "One of the Warriors was attacked by a werewolf and torn horribly on a trip to a Settlement, and upon arrival discovered he was infected by the Curse of the Wolf. Hurled from the City, the Warrior sought an answer for the cure to this curse, and with his friends learned of the Fountain of Light buried deep in the mountains. Supposedly enchanted with great power, this Fount is able to heal any affliction, remove any curse, for a price. With little else to try, the Warriors set out for the Fountain of Light, racing against the full moon.", 100000, 50)));
		resp.getWriter().println(createQuest(new Task("The Skaven Priests of Torne", "Princess Janitta, the daughter of King Reinwelle, has been captured by the evil skavens that control the sewers beneath the ancient city of Torne. The warriors have been commissioned to rescue her before she is sacrificed by the plague priests in their temple hidden somewhere deep within the meandering sewer passageways. ", 240000, 120)));
		resp.getWriter().println(createQuest(new Task("The Dragon Oracle", "High above the city of Khost is a dragon’s cave, where the monster will answer questions for a price. Only the bravest of Warriors can hope to reach the dragon and learn their answer, for he keeps the dungeon well stocked with creatures. The Warriors have been hired to reach the dragon and ask a single question: is the treaty offered by a nearby count trustworthy or not? The Duke hiring the Warriors pays well, an objective room treasure and 50 gold each.", 2*60*60*1000, 50000)));
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
				"You adveture leads you to the temple of Belk. There you feel inclined to sit in the back on the first floor and begin your studies." +
				" You start by checking a very large encyclopedia to see what more you can learn about dragons.", "dragonknowledge",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "Congratulations! You are well on your way to becoming a great dragon trainer!.","",
                itemList, 200);
		questList.add(quest1);
		
		QuestEntity quest2 = new QuestEntity("Wait..what do dragons eat again?", "You are still in the process of understanding your role as a dragon trainer. " +
				"Dragons, like other animals, need food but what do dragons eat? And where do you go to find it? Luckily you over hear another dragon trainer mention something " +
				"about going to the Mosely market to pick up their dragon some food for the road. You decide to travel to Mosely market to find some food for your dragon" +
				" no matter what. Even if that means looking under every table.", "dragonchow",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "Delicious! Is what your dragon would say if it could talk... but you know deep inside it enjoyed the food you brought it.","",
                itemList, 200);
		questList.add(quest2);
		
		QuestEntity quest3 = new QuestEntity("Dragon Trainer's Liscense: Part 1", "Hmmmm, who would have thought you needed a liscense to train a dragon. Obtaining a " +
				" liscense shouldn't be too hard though, right? You decide to head to the castle of mathematicians and futuristic scientists who have the ability" +
				" to program these wierd machines that look like boxes of light. What was the name of that castle again? You decide to sit and ponder the name" +
				" before you venture out for a liscense. Knowing the name of the castle first will make it easier to find", "Duke",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "It seems like you're at the right place. As you walk in you take a right and notice a room of futuristic scientist hard at work on their weird light boxes.","",
                itemList, 100);
		questList.add(quest3);
		
		QuestEntity quest4 = new QuestEntity("Dragon Trainer's Liscense: Part 2", " After walking past the room with light boxes you notice a seating area." +
				" You figure if you just wait here someone will assist you momentarily and give you a dragon trainer's liscense. To past the time you" +
				" look through some left out scrolls on the table. What's this? You see a letter stating that those who wish to obtain a dragon liscense must" +
				" speak to a sorceress by the name of...without second thought you write down the name and go to seek out this person.", "Haldas Lunn Von",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                " Haladas Lunn Von, hmmm you have never heard of her. And there is no record of her ever existing in the Land of Elon, you take a closer look at the name.","",
                itemList, 150);
		questList.add(quest4);
		
		QuestEntity quest5 = new QuestEntity("Dragon Trainer's Liscense: Part 3", " Who is this sorceress? At this rate you'll never be able to get a liscense." +
				" Perhaps if you go back to the castle where you found this letter you will be able to find her. Checking the name tags on the outside of" +
				" the room doors seems like a good idea. You look at the name one more time...hmmm is this possibly an anagram of some sort?", "Shannon Duvall",
                200, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                " Yes! You have figured it out. Haladas Lunn Vonn turns out to be an anagram for Shannon Duvall! She will be able to give you a dragon trainer's liscense!","",
                itemList, 200);
		questList.add(quest5);
		
		QuestEntity quest6 = new QuestEntity("Dragon Trainer's Liscense: Part 4", " You finally find the sorceress who will provide you with a dragon trainer's liscense." +
				" She is reluctant to give you the liscense. After some debate decides to give you a liscense if and only if you can answer a question for her." +
				" What was the first light box (computer) bug? You respond with an answer, hopefully you're right or you won't be able to get your dragon trainer's liscense.", "zuztye",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                " Awesome, a moth was the correct answer. You have finally recived your dragon trainer's liscense! Look out dragon trainers there is a new kid in town... ","",
                itemList, 100);
		questList.add(quest6);
		
		QuestEntity quest7 = new QuestEntity("Wealth flows in the Land: Part 1"," While exploring The Land of Oaks you notice a change." +
				" The castle formally named castle A has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advetageous" +
				" to know the wealthy in this land as they are fans of the dragon tournaments and often sponser young trainers they know and like.", "Williams",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                " You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 100);
		questList.add(quest7);
		
		QuestEntity quest8 = new QuestEntity("Wealth flows in the Land: Part 2"," While continuing to explore The Land of Oaks you notice another change." +
				" The castle formally named castle E has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advetageous" +
				" to know the wealthy in this land as they are fans of the dragon tournaments and often sponser young trainers they know and like.", "Council",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                " You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 100);
		questList.add(quest8);
		
		QuestEntity quest9 = new QuestEntity("Wealth flows in the Land: Part 3"," While continuing to explore The Land of Oaks you notice another change." +
				" The castle formally named castle E has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advetageous" +
				" to know the wealthy in this land as they are fans of the dragon tournaments and often sponser young trainers they know and like.", "Brown",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 100);
		questList.add(quest9);
		
		QuestEntity quest10 = new QuestEntity("Wealth flows in the Land: Part 4"," While continuing to explore The Land of Oaks you notice another change." +
				" The castle formally named castle F has been bought by a monarch and renamed. You decide to take note of this name in your journal as it is advetageous" +
				" to know the wealthy in this land as they are fans of the dragon tournaments and often sponser young trainers they know and like.", "Sullivan",
                100, 0,
                "5-9-13 3:22am",
                "This quest will not expire",
                "You didn't get a chance to meet with the monarch but your name is now known at this castle. Could this be a possible sponsor for the future?","",
                itemList, 100);
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
