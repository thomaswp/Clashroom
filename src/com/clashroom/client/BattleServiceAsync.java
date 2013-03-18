package com.clashroom.client;

import java.util.List;

import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.data.BattleEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BattleServiceAsync {
	void getBattle(long id, AsyncCallback<BattleEntity> callback)
			throws IllegalArgumentException;
	void getBattles(AsyncCallback<List<BattleEntity>> callback)
			throws IllegalArgumentException;
}
