package com.clashroom.shared.news;

import java.util.Date;
import java.util.List;

import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class JoinNews extends NewsfeedItem {
	private static final long serialVersionUID = 1L;
	
	private String name, dragonName;
	private long id;
	
	@Deprecated
	public JoinNews() { super(null, (List<Long>) null); }
	
	public JoinNews(UserEntity user) {
		super(new Date(), user.getId());
		name = user.getUsername();
		dragonName = user.getDragon().getName();
		id = user.getId();
	}

	@Override
	public Widget getInfoWidget() {
		return new HTML(Formatter.format(
				"<a href='#%s?id=%s'>%s</a> has joined the adventure with a dragon named %s!", 
				UserInfoPage.NAME, id, name, dragonName));
	}

	@Override
	public String getName() {
		return "Join";
	}

}
