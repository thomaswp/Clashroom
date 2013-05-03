package com.clashroom.client.services;

import java.util.List;

import com.clashroom.shared.entity.BattleEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BattleServiceAsync {
	void getBattle(long id, AsyncCallback<BattleEntity> callback)
			throws IllegalArgumentException;
	void getBattles(AsyncCallback<List<BattleEntity>> callback)
			throws IllegalArgumentException;
	void createBattle(List<Long> teamAIds, List<Long> teamBIds, AsyncCallback<Long> callback);
}
