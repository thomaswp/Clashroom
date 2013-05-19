package com.clashroom.shared.news;

import java.util.Date;
import java.util.List;

import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.Formatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a messege in the news feed for the town herald whenever
 * a user completes a quest
 * */
public class QuestNews extends NewsfeedItem {
	
	private static final long serialVersionUID = 1L;
	
	private String questName;
	private String userName;
	private long userId;
	
	@Deprecated
	public QuestNews(){
		super(null,(List<Long>)null);
	}
	/**
	 * Constructor
	 * 
	 * @param Date date
	 * @param String questName
	 * @param String username
	 * @param Long id
	 * */
	public QuestNews(Date date, String questName, String username, Long id) {
		super(date,id);
		this.questName = questName;
		this.userName = username;
		this.userId = id;
	}
	

	public QuestNews(Date date, List<Long> playerIds) {
		super(date, playerIds);
	}

	@Override
	public Widget getInfoWidget() {
		String name;
		if (userId > 0) {
			name = UserInfoPage.getHTMLLinkToUser(userName, userId);
		} else {
			name = userName;
		}
		return new HTML(Formatter.format("%s has successfuly completed the quest \"%s.\"", 
				name, questName));
	}

	@Override
	public String getName() {
		return "Quest";
	}
}
