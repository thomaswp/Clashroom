package com.clashroom.shared.news;

import java.util.Date;
import java.util.List;

import com.clashroom.shared.task.ActiveTask;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TaskNews extends NewsfeedItem {
	private static final long serialVersionUID = 1L;
	private ActiveTask task;
	
	@Deprecated
	public TaskNews() { 
		super(null, (List<Long>)null);
	}
	
	public TaskNews(ActiveTask task, long userID) {
		super(new Date(), userID);
		this.task = task;
	}

	@Override
	public Widget getInfoWidget() {
		SimplePanel panel = new SimplePanel();
		panel.add(new Label("You completed " + task.getTitle() + " and received " + task.getReward() + " xp!"));
		return panel;
	}
}
