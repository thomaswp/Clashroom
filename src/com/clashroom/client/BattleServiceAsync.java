package com.clashroom.client;

import java.util.List;

import com.clashroom.shared.BattleFactory;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BattleServiceAsync {
	void getBattle(long id, AsyncCallback<BattleFactory> callback)
			throws IllegalArgumentException;
	void getBattles(AsyncCallback<List<BattleFactory>> callback)
			throws IllegalArgumentException;
}
