package com.clashroom.shared.news;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.clashroom.client.battle.BattlePage;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.entity.BattleEntity;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class BattleNews extends NewsfeedItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private long battleId;
	private String winner, loser;
	private long winnerId, loserId;
	
	private static List<Long> combine(List<Long> listA, List<Long> listB) {
		ArrayList<Long> list = new ArrayList<Long>();
		list.addAll(listA);
		list.addAll(listB);
		return list;
	}
	
	@Deprecated
	public BattleNews() { 
		super(null, (List<Long>)null);
	}
	
	public BattleNews(BattleEntity battle) {
		super(battle.getDate(), combine(battle.getTeamAIds(), battle.getTeamBIds()));
		String teamA = battle.getBattleFactory().getTeamAName();
		String teamB = battle.getBattleFactory().getTeamBName();
		long teamAId = 0, teamBId = 0;
		if (battle.getTeamAIds().size() == 1) teamAId = battle.getTeamAIds().get(0);
		if (battle.getTeamBIds().size() == 1) teamBId = battle.getTeamBIds().get(0);
		winner = battle.isTeamAVictor() ? teamA : teamB; 
		winnerId = battle.isTeamAVictor() ? teamAId : teamBId;
		loser = battle.isTeamAVictor() ? teamB : teamA;
		loserId = battle.isTeamAVictor() ? teamBId : teamAId;
		battleId = battle.getId();
	}

	@Override
	public Widget getInfoWidget() {
		String wName = winnerId == 0 ? winner : 
			Formatter.format("<a href='#%s?id=%s'>%s</a>", UserInfoPage.NAME, winnerId, winner);
		String lName = loserId == 0 ? loser : 
			Formatter.format("<a href='#%s?id=%s'>%s</a>", UserInfoPage.NAME, loserId, loser);
		
		return new HTML(Formatter.format("%s bested %s in a <a href='#%s?id=%s'>battle</a>", 
				wName, lName, BattlePage.NAME, battleId));
	}

	@Override
	public String getName() {
		return "Battle";
	}

}
