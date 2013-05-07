package com.clashroom.client.services;

import java.util.Date;
import java.util.List;

import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.QueuedBattleEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("battle")
public interface BattleService extends RemoteService {
	BattleEntity getBattle(long id);
	List<BattleEntity> getBattles();
	List<QueuedBattleEntity> getScheduledBattles();
	Long createBattle(List<Long> teamAIds, List<Long> teamBIds);
	Long scheduleBattle(String teamAName, List<Long> teamAIds, String teamBName, 
			List<Long> teamBIds, Date time);
}
