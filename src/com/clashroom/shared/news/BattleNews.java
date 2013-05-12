package com.clashroom.shared.news;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.client.battle.BattlePage;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.entity.BattleEntity;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class BattleNews extends NewsfeedItem {
	private static final long serialVersionUID = 1L;

	private long battleId;
	private String winner, loser;
	
	
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
		winner = battle.isTeamAVictor() ? teamA : teamB;
		loser = battle.isTeamAVictor() ? teamB : teamA;
		battleId = battle.getId();
	}

	@Override
	public Widget getInfoWidget() {
		return new HTML(Formatter.format("%s bested %s in a <a href='#%s?id=%s'>battle</a>", 
				winner, loser, BattlePage.NAME, battleId));
	}

}
