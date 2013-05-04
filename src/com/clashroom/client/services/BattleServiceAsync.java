package com.clashroom.client.services;

import java.util.Date;
import java.util.List;

import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.QueuedBattleEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BattleServiceAsync {
	void getBattle(long id, AsyncCallback<BattleEntity> callback)
			throws IllegalArgumentException;
	void getBattles(AsyncCallback<List<BattleEntity>> callback)
			throws IllegalArgumentException;
	void getScheduledBattles(AsyncCallback<List<QueuedBattleEntity>> callback)
			throws IllegalArgumentException;
	void createBattle(List<Long> teamAIds, List<Long> teamBIds, AsyncCallback<Long> callback);
	void scheduleBattle(String teamAName, List<Long> teamAIds, String teamBName, 
			List<Long> teamBIds, Date time, AsyncCallback<Long> callback);
}
