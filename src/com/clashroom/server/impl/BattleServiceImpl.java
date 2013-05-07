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

public class BattleServiceImpl extends RemoteServiceServlet implements BattleService {
	private static final long serialVersionUID = 1L;

	@Override
	public BattleEntity getBattle(long id) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		BattleEntity entity = pm.getObjectById(BattleEntity.class, id);
		entity.getBattleFactory();
		entity = pm.detachCopy(entity);
		pm.close();
		return entity;
	}

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
			entity.getBattleFactory();
			entities.add(pm.detachCopy(entity));
		}
		pm.close();
		return entities;
	}
	
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
	
	@Override
	public Long scheduleBattle(String teamAName, List<Long> teamAIds, 
			String teamBName, List<Long> teamBIds, Date time) {
		QueuedBattleEntity qb = new QueuedBattleEntity(teamAName, teamAIds, teamBName, teamBIds, time);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(qb);
		pm.close();
		return qb.getId();
	}
	
	@Override
	public Long createBattle(List<Long> teamAIds, List<Long> teamBIds) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Long id = createBattleImpl(pm, teamAIds, teamBIds, new Date());
		pm.close();
		return id;
	}


	public static void createBattleImpl(PersistenceManager pm, QueuedBattleEntity qb) {
		createBattleImpl(pm, qb.getTeamAIds(), qb.getTeamBIds(), qb.getTime());
	}
	
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
		
		NewsfeedEntity item = new NewsfeedEntity(new BattleNews(battleEntity));
		pm.makePersistent(item);
		
		pm.flush();
		
		return battleEntity.getId();
	}
	
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
			factory.addPostBattleAction(new ActionExp(db, exp, dragon.getLevel()));
		}
	}
}
