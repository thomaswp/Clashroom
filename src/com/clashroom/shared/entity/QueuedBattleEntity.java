package com.clashroom.shared.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * An entity representing a {@link BattleEntity} that should be
 * created in the future.
 */
@PersistenceCapable
public class QueuedBattleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private List<Long> playerIds;
	@Persistent
	private String teamAName;
	@Persistent
	private List<Long> teamAIds;
	@Persistent
	private String teamBName;
	@Persistent
	private List<Long> teamBIds;
	@Persistent
	private Date time;
	
	@Deprecated
	public QueuedBattleEntity() { }
	
	public QueuedBattleEntity(String teamAName, List<Long> teamAIds,
			String teamBName, List<Long> teamBIds, Date time) {
		this.teamAName = teamAName;
		this.teamAIds = teamAIds;
		this.teamBName = teamBName;
		this.teamBIds = teamBIds;
		this.time = time;
		
		playerIds = new LinkedList<Long>();
		playerIds.addAll(teamAIds);
		playerIds.addAll(teamBIds);
	}

	public Long getId() {
		return id;
	}
	
	public List<Long> getPlayerIds() {
		return playerIds;
	}
	
	public String getTeamAName() {
		return teamAName;
	}

	public List<Long> getTeamAIds() {
		return teamAIds;
	}

	public String getTeamBName() {
		return teamBName;
	}

	public List<Long> getTeamBIds() {
		return teamBIds;
	}

	public Date getTime() {
		return time;
	}
}
