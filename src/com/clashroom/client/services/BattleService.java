package com.clashroom.client.services;

import java.util.List;

import com.clashroom.shared.entity.BattleEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("battle")
public interface BattleService extends RemoteService {
	BattleEntity getBattle(long id) throws IllegalArgumentException;
	List<BattleEntity> getBattles() throws IllegalArgumentException;
}
