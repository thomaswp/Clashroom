package com.clashroom.client.services;

import java.util.Date;
import java.util.List;

import com.clashroom.server.impl.BattleServiceImpl;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.QueuedBattleEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * See {@link BattleServiceImpl}
 */
public interface BattleServiceAsync {
	/** See {@link BattleServiceImpl#getBattle(long)} */
	void getBattle(long id, AsyncCallback<BattleEntity> callback)
			throws IllegalArgumentException;
	/** See {@link BattleServiceImpl#getBattles()} */
	void getBattles(AsyncCallback<List<BattleEntity>> callback)
			throws IllegalArgumentException;
	/** See {@link BattleServiceImpl#getScheduledBattles()} */
	void getScheduledBattles(AsyncCallback<List<QueuedBattleEntity>> callback)
			throws IllegalArgumentException;
	/** See {@link BattleServiceImpl#createBattle(List, List)} */
	void createBattle(List<Long> teamAIds, List<Long> teamBIds, AsyncCallback<Long> callback);
	/** See {@link BattleServiceImpl#scheduleBattle(String, List, String, List, Date)} */
	void scheduleBattle(String teamAName, List<Long> teamAIds, String teamBName, 
			List<Long> teamBIds, Date time, AsyncCallback<Long> callback);
}
