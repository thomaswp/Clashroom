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

/**
 * A {@link NewsfeedItem} for when the player is in a battle.
 */
public class BattleNews extends NewsfeedItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private long battleId;
	private String winner, loser;
	private long winnerId, loserId;
	
	//need a static method to use in the super constructor
	//combines two lists of Longs
	private static List<Long> combine(List<Long> listA, List<Long> listB) {
		ArrayList<Long> list = new ArrayList<Long>();
		list.addAll(listA);
		list.addAll(listB);
		return list;
	}
	
	//Empty constructor is necessary for serialization across RPC
	@Deprecated
	public BattleNews() { 
		super(null, (List<Long>)null);
	}
	
	/**
	 * Constructs a {@link NewsfeedItem} for the given battle occurring
	 * @param battle The battle
	 */
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
		String wName = winnerId == 0 ? winner : UserInfoPage.getHTMLLinkToUser(winner, winnerId);
		String lName = loserId == 0 ? loser : UserInfoPage.getHTMLLinkToUser(loser, loserId);
		String battle = BattlePage.getHTMLLinkToBattle("battle", battleId);
		
		return new HTML(Formatter.format("%s bested %s in a %s!", 
				wName, lName, battle));
	}

	@Override
	public String getName() {
		return "Battle";
	}

}
