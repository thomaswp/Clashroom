package com.clashroom.shared.news;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

/**
 * A base-class for items that appear in the "Town Herald"
 */
public abstract class NewsfeedItem implements Serializable, IsSerializable {
	private static final long serialVersionUID = 1L;
	
	private List<Long> playerIds;
	private Date date;
	
	/**
	 * Gets the date this news happened
	 * @return
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Gets a list of each {@link UserEntity} involved in this news
	 * @return
	 */
	public List<Long> getPlayerIds() {
		return playerIds;
	}
	
	/**
	 * Constructs news happening on the given date to the {@link UserEntity}s
	 * with the given ids
	 * @param date
	 * @param playerIds
	 */
	public NewsfeedItem(Date date, List<Long> playerIds) {
		this.date = date != null ? new Date(date.getTime()) : null;
		this.playerIds = playerIds;
	}
	
	/**
	 * Constructs news happening on the given date to the {@link UserEntity}s
	 * with the given ids
	 * @param date
	 * @param playerIds
	 */
	public NewsfeedItem(Date date, long... playerIds) {
		this(date, new ArrayList<Long>());
		for (long playerId : playerIds) {
			this.playerIds.add(playerId);
		}
	}
	
	/**
	 * Should return a {@link Widget} describing this news
	 * @return
	 */
	public abstract Widget getInfoWidget();
	/**
	 * Currently for debugging pruposes - should return the
	 * kind of news this is: "Battle," "Quest," etc.
	 */
	public abstract String getName();
}
