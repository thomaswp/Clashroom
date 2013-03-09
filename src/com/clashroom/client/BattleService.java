package com.clashroom.client;

import java.util.List;

import com.clashroom.shared.BattleFactory;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("battle")
public interface BattleService extends RemoteService {
	BattleFactory getBattle(long id) throws IllegalArgumentException;
	List<BattleFactory> getBattles() throws IllegalArgumentException;
}
