package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.jsp.ah.searchDocumentBody_jsp;

import com.clashroom.client.services.UserInfoService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.NewsfeedEntity;
import com.clashroom.shared.entity.QuestEntity;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.news.JoinNews;
import com.clashroom.shared.news.NewsfeedItem;
import com.clashroom.shared.news.QuestNews;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Handles datastore requests pertaining to the users.
 */
@SuppressWarnings("serial")
public class UserInfoServiceImpl extends RemoteServiceServlet 
implements UserInfoService {

	/**
	 * Gets the currently logged in user
	 * @param pm
	 * @return
	 */
	public static UserEntity getCurrentUser(PersistenceManager pm) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user == null) return null;
		
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		return entity;
	}
	
	/**
	 * Gets the {@link UserEntity} with the given id.
	 * @param id The id of the requested User
	 */
	@Override
	public UserEntity getUser(long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "id == %s", id);
			entity = pm.detachCopy(entity); //Must detach Entities to send them across RPC
			return entity;
		} finally {
			pm.close();
		}
	}
	
	/**
	 * Gets the {@link UserEntity} for the currently logged in user.
	 * If it does not exist, returns a new UserEntity, with the
	 * {@link UserEntity#isSetup()} property set to false.
	 */
	@Override
	public UserEntity getUser() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user == null) return null;

		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		
		if (entity == null) {
			entity = new UserEntity(user.getEmail());
		} else {
			entity = pm.detachCopy(entity);
		}
		
		pm.flush();
		pm.close();
		
		return entity;
	}
	
	/**
	 * Gets all {@link UserEntity}s in the datastore.
	 */
	@Override
	public List<UserEntity> getAllUsers() {
		LinkedList<UserEntity> entities = new LinkedList<UserEntity>();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<UserEntity> attachedEntities = QueryUtils.query(pm, UserEntity.class, "");
		
		for (UserEntity entity : attachedEntities) {
			entities.add(pm.detachCopy(entity)); //must detach to send across RPC
		}
		
		return entities;
	}

	/**
	 * Persists a {@link UserEntity} to the datastore which
	 * <b>does not yet exist</b>. The UserEntity will presumably
	 * not yet have an id.
	 * @param user
	 */
	@Override
	public void addUser(UserEntity user) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (QueryUtils.query(pm, UserEntity.class, "email==%s", user.getEmail()).size() != 0) {
			Debug.write("User already exists");
			return;
		}
		
		DragonEntity dragon = user.getDragon();
		
		//Start gain the Dragon's initial stats
		DragonClass dragonClass = DragonClass.getById(dragon.getDragonClassId());
		dragonClass.setUp(dragon);

		pm.makePersistent(user);
		
		//Add some news that a new player joined the game
		JoinNews joinNews = new JoinNews(user);
		NewsfeedEntity newsEntity = new NewsfeedEntity(joinNews);
		pm.makePersistent(newsEntity);
		
		pm.flush();
		pm.close();
	}

	/**
	 * Has the current {@link UserEntity} learn the {@link Skill}
	 * with the given id. Will fail if the user's {@link DragonEntity}
	 * is not a high enough level or if the user does not have enough
	 * skill points. The user's skill points will be appropriately
	 * reduced after the Skill is learned.
	 * 
	 * @param id The id of the Skill to learn
	 */
	@Override
	public void learnSkill(int id) {

		Skill skill = Skill.getById(id);
		if (skill == null) {
			throw new RuntimeException("No skill with id " + id);
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		UserEntity user = getCurrentUser(pm);
		if (user == null) {
			pm.close();
			throw new RuntimeException("User not logged in!");
		}
		
		if (user.getSkillPoints() < skill.getSkillPointCost()) {
			pm.close();
			throw new RuntimeException("Not enough skill points!");
		}
		if (user.getDragon().getSkills().contains(skill.getId())) {
			pm.close();
			throw new RuntimeException("Skill already learned!");
		}
		
		user.setSkillPoints(user.getSkillPoints() - skill.getSkillPointCost());
		//Because Dragons are EmbeddedEntities we've found it is necessary to detach a copy
		//before modifying in order to update the datastore with the changes
		//This essentially forces the the UserEntity to update it's dragon field.
		DragonEntity dragon = pm.detachCopy(user.getDragon());
		dragon.getSkills().add(skill.getId());
		user.setDragon(dragon);
		
		pm.makePersistent(user);
		
		pm.flush();
		pm.close();
	}

	/**
	 * Gets up to the given count of {@link NewsfeedItem}s from the
	 * datastore which involve one of the given users. If users is
	 * null, simply returns the most recent news items, up to count.
	 * 
	 * @param users a list of ids of users, one of which the returned news
	 * should involve
	 * @param count the maximum number of results to return
	 */
	@Override
	public List<NewsfeedItem> getNews(List<Long> users, int count) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		List<NewsfeedEntity> entities = new ArrayList<NewsfeedEntity>();
		if (users != null) {
			for (Long userId : users) {
				entities.addAll(QueryUtils.query(pm, NewsfeedEntity.class, "playerIds.contains(%s)", userId));
			}

			//TODO: Better implementation than just grabbing everything...
			Collections.sort(entities, new Comparator<NewsfeedEntity>() {
				@Override
				public int compare(NewsfeedEntity o1, NewsfeedEntity o2) {
					return o1.getDate().compareTo(o2.getDate());
				}
			});
		} else {
			entities.addAll(QueryUtils.queryRange(pm, NewsfeedEntity.class, "date desc", count, null));
		}
		
		List<NewsfeedItem> items = new ArrayList<NewsfeedItem>();
		for (int i = 0; i < count && i < entities.size(); i++) {
			items.add(entities.get(i).getItem());
		}
		
		pm.close();
		return items;
	}

	/**
	 * Gives the current {@link UserEntity} experience, leveling
	 * up if appropriate.
	 * @param exp The amount of experience to gain
	 */
	@Override
	public void addExp(int exp) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		addExpImpl(pm, exp);
		pm.close();
	}
	
	/**
	 * A static method, allowing the currently logged in {@link UserEntity}
	 * to gain the given amount of experience, leveling up if appropriate.
	 * This allows other server-side code to add experience to the user.
	 * This method handles retrieving and persisting the {@link DragonEntity},
	 * so it would be inappropriate to use it in a situation where the user has
	 * already been retrieved.
	 * <p/>
	 * <b>Do not use this method</b> if you already have a detached {@link DragonEntity}.
	 * Instead, simply call {@link DragonEntity#addExp(int)}.
	 * 
	 * @param pm The PersistenceManager to use for this transaction
	 * @param exp The amount of experience to gain
	 */
	public static void addExpImpl(PersistenceManager pm, int exp) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		Debug.write("Exp gained: %d", exp);
		
		if (user == null) return;
		
		UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email == %s", user.getEmail());
		
		if (entity == null) {
			throw new RuntimeException("No UserEntity");
		}
		entity.getDragon().addExp(exp);
		//see learnSkill() for why to detach a copy
		entity.setDragon(pm.detachCopy(entity.getDragon()));
		
		pm.makePersistent(entity);
	}
	
	/**
	 * Has the currently logged in {@link UserEntity} finish the
	 * {@link QuestEntity} with the given id.
	 * @param id The id
	 */
	@Override
	public void completeQuest(long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//The try/finally block ensure the pm is closed no matter what
		try {
			UserEntity user = getUser();
			if (user == null) throw new RuntimeException("No user");

			QuestEntity quest = QueryUtils.queryUnique(pm, QuestEntity.class, "id==%s", id);
			if (quest == null) throw new RuntimeException("No quest with id :" + id);

			if (user.getCompletedQuests().contains(id)) throw new RuntimeException("Quest already completed!");

			user.addCompletedQuest(id);
			user.setCompletedQuests(user.getCompletedQuests()); //Ensure the UserEntity updates this field
			for(Long itemID: quest.getItemsRewarded()){
				user.addItemToInventory(itemID);
			}
			user.setItemsIventory(user.getItemInventory());  //Ensure the UserEntity updates this field
			int totalsp = user.getSkillPoints() + quest.getQuestPoints();

			user.setSkillPoints(totalsp);

			user.getDragon().addExp(quest.getExperienceRewarded());

			//Add some news that this quest was completed
			QuestNews qn = new QuestNews(new Date(), quest.getQuestName(), user.getUsername(), user.getId());
			NewsfeedEntity ne = new NewsfeedEntity(qn);
			pm.makePersistent(ne);

			pm.makePersistent(user);
		} finally {
			pm.close();
		}

	}
}
