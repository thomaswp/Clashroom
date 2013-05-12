package com.clashroom.shared.news;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

public abstract class NewsfeedItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Long> playerIds;
	private Date date;
	
	public Date getDate() {
		return date;
	}
	
	public List<Long> getPlayerIds() {
		return playerIds;
	}
	
	public NewsfeedItem(Date date, List<Long> playerIds) {
		this.date = date != null ? new Date(date.getTime()) : null;
		this.playerIds = playerIds;
	}
	
	public NewsfeedItem(Date date, long... playerIds) {
		this(date, new ArrayList<Long>());
		for (long playerId : playerIds) {
			this.playerIds.add(playerId);
		}
	}
	
	public abstract Widget getInfoWidget();

}
