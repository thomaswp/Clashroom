package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.clashroom.client.services.BattleService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.battle.Battle;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.actions.ActionExp;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.battlers.DragonBattler;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.NewsfeedEntity;
import com.clashroom.shared.entity.QueuedBattleEntity;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.news.BattleNews;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * An RPC service for manipulating {@link BattleEntity}s.
 */
public class BattleServiceImpl extends RemoteServiceServlet implements BattleService {
	private static final long serialVersionUID = 1L;

	/**
	 * Retrieves the {@link BattleEntity} with the given id.
	 * @param id The id
	 */
	@Override
	public BattleEntity getBattle(long id) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		BattleEntity entity = pm.getObjectById(BattleEntity.class, id);
		entity.getBattleFactory(); //Get the BattleFactory to ensure it is retrieved from the datastore
		entity = pm.detachCopy(entity); //Detach the entity, to allow it to be passed over RPC
		pm.close();
		return entity;
	}

	/**
	 * Gets all {@link BattleEntity}s in the datastore which involve
	 * the currently logged in user.
	 */
	@Override
	public List<BattleEntity> getBattles() throws IllegalArgumentException {
		ArrayList<BattleEntity> entities = new ArrayList<BattleEntity>();
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) {
			return entities;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity userEntity = QueryUtils.queryUnique(pm, UserEntity.class, "email==%s", user.getEmail());
		
		List<BattleEntity> queryEntities = QueryUtils.query(
				pm, BattleEntity.class, "playerIds.contains(%s)", userEntity.getId());
		for (BattleEntity entity : queryEntities) { 
			entity.getBattleFactory(); //Get the BattleFactory to ensure it is retrieved from the datastore
			entities.add(pm.detachCopy(entity)); //Detach the entity, to allow it to be passed over RPC
		}
		pm.close();
		return entities;
	}
	
	/**
	 * Gets all {@link QueuedBattleEntity} which involve the currently logged in user.
	 */
	@Override
	public List<QueuedBattleEntity> getScheduledBattles() {
		ArrayList<QueuedBattleEntity> entities = new ArrayList<QueuedBattleEntity>();
		
		User user = UserServiceFactory.getUserService().getCurrentUser();
		if (user == null) {
			return entities;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		UserEntity userEntity = QueryUtils.queryUnique(pm, UserEntity.class, "email==%s", user.getEmail());
		
		List<QueuedBattleEntity> queryEntities = QueryUtils.query(
				pm, QueuedBattleEntity.class, "playerIds.contains(%s)", 
				userEntity.getId(), userEntity.getId());
		for (QueuedBattleEntity entity : queryEntities) {
			entities.add(pm.detachCopy(entity));
		}
		pm.close();
		return entities;
	}
	
	/**
	 * Adds a new {@link QueuedBattleEntity} with the given opposing teams
	 * for the given date.
	 * @param teamAName The name of the left-side team
	 * @param teamAIds A list of ids of the {@link UserEntity}s which comprise teamA
	 * @param teamBName The name of the right-side team
	 * @param teamBIds A list of ids of the {@link UserEntity}s which comprise teamB
	 */
	@Override
	public Long scheduleBattle(String teamAName, List<Long> teamAIds, 
			String teamBName, List<Long> teamBIds, Date time) {
		QueuedBattleEntity qb = new QueuedBattleEntity(teamAName, teamAIds, teamBName, teamBIds, time);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(qb);
		pm.close();
		return qb.getId();
	}
	
	/**
	 * Creates a new {@link BattleEntity} with the given teams. The battle
	 * is automatically run and the appropriate experience is given to the
	 * participants.
	 * 
	 * @param teamAIds A list of ids of the {@link UserEntity}s which comprise teamA 
	 * @param teamBIds A list of ids of the {@link UserEntity}s which comprise teamB
	 */
	@Override
	public Long createBattle(List<Long> teamAIds, List<Long> teamBIds) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Long id = createBattleImpl(pm, teamAIds, teamBIds, new Date());
		pm.close();
		return id;
	}


	/**
	 * Uses the given {@link QueuedBattleEntity} to call 
	 * {@link BattleServiceImpl#createBattleImpl(PersistenceManager, List, List, Date)}
	 * with the appropriate arguments.
	 * @param pm The PersistenceManager to use for this transaction
	 * @param qb The QueuedBatteEntity to use to fill the parameters
	 * @return
	 */
	public static Long createBattleImpl(PersistenceManager pm, QueuedBattleEntity qb) {
		return createBattleImpl(pm, qb.getTeamAIds(), qb.getTeamBIds(), qb.getTime());
	}
	
	/**
	 * Creates a {@link BattleEntity} pitting the given teams against each other,
	 * running the battle and adding the appropriate experience to both sides.
	 * @param pm The PersistenceManager to use for the transaction
	 * @param teamAIds A list of ids of the {@link UserEntity}s which comprise teamA 
	 * @param teamBIds A list of ids of the {@link UserEntity}s which comprise teamB
	 * @param time The time the given battle should [take/have taken] place. Regardless
	 * the battle will execute immediately, but the date will show up as provided
	 * @return The id of the created BattleEntity, or null if the battle failed to create
	 */
	public static Long createBattleImpl(PersistenceManager pm, List<Long> teamAIds, 
			List<Long> teamBIds, Date time) {
		LinkedList<UserEntity> teamAEntities = new LinkedList<UserEntity>();
		LinkedList<UserEntity> teamBEntities = new LinkedList<UserEntity>();
		
		for (Long id : teamAIds) {
			UserEntity userEntity = QueryUtils.queryUnique(pm, UserEntity.class, "id==%s", id);
			if (userEntity == null || !userEntity.isSetup()) return null;
			teamAEntities.add(userEntity);
		}
		
		for (Long id : teamBIds) {
			UserEntity userEntity = QueryUtils.queryUnique(pm, UserEntity.class, "id==%s", id);
			if (userEntity == null || !userEntity.isSetup()) return null;
			teamBEntities.add(userEntity);
		}
		
		LinkedList<Battler> teamA = new LinkedList<Battler>(), teamB = new LinkedList<Battler>();
		
		String teamAName = Battle.getTeamName(teamAEntities);
		for (UserEntity userEntity : teamAEntities) {
			DragonBattler db = new DragonBattler(userEntity.getDragon(), userEntity.getId());
			teamA.add(db);
		}
		
		String teamBName = Battle.getTeamName(teamBEntities);
		for (UserEntity userEntity : teamBEntities) {
			DragonBattler db = new DragonBattler(userEntity.getDragon(), userEntity.getId());
			teamB.add(db);
		}
				
		if (teamA.isEmpty() || teamB.isEmpty()) return null;
		
		BattleFactory factory = new BattleFactory(teamAName, teamA, teamBName, teamB);
		BattleEntity battleEntity = new BattleEntity(factory, time);
		
		gainExp(pm, teamAEntities, teamA, battleEntity.getTeamAExp(), factory);
		gainExp(pm, teamBEntities, teamB, battleEntity.getTeamBExp(), factory);
		
		pm.makePersistent(battleEntity);
		
		//Add some news that this happened
		NewsfeedEntity item = new NewsfeedEntity(new BattleNews(battleEntity));
		pm.makePersistent(item);
		
		pm.flush();
		
		return battleEntity.getId();
	}
	
	//Has the given team gain its experience from the battle
	private static void gainExp(PersistenceManager pm, List<UserEntity> users, 
			List<Battler> team, int exp, BattleFactory factory) {
		for (int i = 0; i < users.size(); i++) {
			UserEntity userEntity = users.get(i);
			Battler db = team.get(i);
			DragonEntity dragon = userEntity.getDragon();
			dragon = pm.detachCopy(dragon);
			dragon.addExp(exp);
			userEntity.setDragon(dragon);
			pm.makePersistent(userEntity);
			//Add some text at the end of the battle, saying that
			//the player gained some experience
			factory.addPostBattleAction(new ActionExp(db, exp, dragon.getLevel()));
			//TODO: There seems to be a bug, either in this logic or in BattlePage's
			//logic, which prevents this message from showing up sometimes at the end
			//of a battle. It is unclear whether the experience is gained or not.
		}
	}
}
