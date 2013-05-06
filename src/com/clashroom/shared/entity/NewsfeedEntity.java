package com.clashroom.shared.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.clashroom.shared.news.NewsfeedItem;

@PersistenceCapable
public class NewsfeedEntity {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private List<Long> playerIds = new LinkedList<Long>();
	
	@Persistent
	private Date date;
	
	@Persistent(serialized="true")
	private NewsfeedItem item;
	
	public NewsfeedEntity(NewsfeedItem item) {
		this.item = item;
		this.date = item.getDate();
		playerIds.addAll(item.getPlayerIds());
	}

	public Long getId() {
		return id;
	}

	public List<Long> getPlayerIds() {
		return playerIds;
	}

	public Date getDate() {
		return date;
	}

	public NewsfeedItem getItem() {
		return item;
	}
}
