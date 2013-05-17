package com.clashroom.shared.news;

import java.util.Date;
import java.util.List;

import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link NewsfeedItem} for when a player joins the game
 * for the first time.
 */
public class JoinNews extends NewsfeedItem {
	private static final long serialVersionUID = 1L;
	
	private String name, dragonName;
	private long id;
	
	//An empty constructor is necessary for serialization
	@Deprecated
	public JoinNews() { super(null, (List<Long>) null); }
	
	/**
	 * Creates a news for the given player joining the game
	 * @param user The user
	 */
	public JoinNews(UserEntity user) {
		super(new Date(), user.getId());
		name = user.getUsername();
		dragonName = user.getDragon().getName();
		id = user.getId();
	}

	@Override
	public Widget getInfoWidget() {
		return new HTML(Formatter.format(
				"%s has joined the adventure with a dragon named %s!", 
				UserInfoPage.getHTMLLinkToUser(name, id), dragonName));
	}

	@Override
	public String getName() {
		return "Join";
	}

}
