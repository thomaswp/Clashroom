package com.clashroom.shared.news;

import java.util.Date;
import java.util.List;

import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.task.ActiveTask;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A {@link NewsfeedItem} for when the player completes a
 * {@link Task}
 * @author deagle
 *
 */
public class TaskNews extends NewsfeedItem {
	private static final long serialVersionUID = 1L;
	private ActiveTask task;
	private String username;
	private long userId;
	
	//Empty constructor is necessary for serialization across RPC
	@Deprecated
	public TaskNews() { 
		super(null, (List<Long>)null);
	}
	
	/**
	 * Constructs a {@link NewsfeedItem} for a given player's completion
	 * of a given {@link Task}
	 * @param task the completed Task
	 * @param userID the player
	 * @param username the player's username
	 */
	public TaskNews(ActiveTask task, long userID, String username) {
		super(new Date(), userID);
		this.task = task;
		this.username = username;
		this.userId = userID;
	}

	@Override
	public Widget getInfoWidget() {
		SimplePanel panel = new SimplePanel();
		String name;
		if (username != null) {
			name = UserInfoPage.getHTMLLinkToUser(username, userId);
		} else {
			name = "You";
		}
		panel.add(new HTML(Formatter.format("%s completed %s and received %sxp!", 
				name, task.getTitle(), task.getReward())));
		return panel;
	}

	@Override
	public String getName() {
		return "Task";
	}
}
