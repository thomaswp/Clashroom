package com.clashroom.shared.news;

import java.util.Date;
import java.util.List;

import com.clashroom.shared.Formatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class QuestNews extends NewsfeedItem {
	
	private static final long serialVersionUID = 1L;
	
	private String questName;
	private String userName;
	
	@Deprecated
	public QuestNews(){
		super(null,(List<Long>)null);
	}

	public QuestNews(Date date, String questName, String username, Long id) {
		super(date,id);
		this.questName = questName;
		this.userName = username;
	}
	

	public QuestNews(Date date, List<Long> playerIds) {
		super(date, playerIds);
	}

	@Override
	public Widget getInfoWidget() {
		return new HTML(Formatter.format("%s has successfuly completed %s quest", 
				userName,questName));
	}

	@Override
	public String getName() {
		return "Quest";
	}
}
