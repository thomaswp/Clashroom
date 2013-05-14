/**
 * QuestImpl.java 1.0 Mar 7, 2013	
 *
 * COPYRIGHT (c) 2013 Riese P. Narcisse. All Rights Reserved 
 */
package com.clashroom.shared.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Quest Class Implementation that will be stored in the database
 * 
 * @author Rpn
 * @version 1.0
 * 
 */
@PersistenceCapable
public class QuestEntity implements Serializable {

    @Persistent
    private String completionCode;
    @Persistent
    private String dateAvailable;
    @Persistent
    private String dateUnavailable;
    @Persistent
    private int experienceRewarded;
    @Persistent
    private boolean globallyAvailable;
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    private List<Long> itemsRewarded;
    @Persistent
    private int questPoints;
	@Persistent
    private int levelRequirement;
    @Persistent
    private String prereqQuest;// TODO: String for now but will be a
    // a key to another quest in the datastore
    @Persistent
    private String questDescription;
    @Persistent
    private String questName;
    @Persistent
    private String victoryText;
    @Persistent
    private boolean questCompleted;

    /**
     * Zero arguement constructor in order to make the class
     * serializable
     */
    public QuestEntity() {

    }

    /**
     * Simple Constructor that takes only the quest name as a
     * parameter
     * 
     * @param questName
     */
    public QuestEntity(String questName) {
        this.questName = questName;
    }

    /**
     * More complex construtor taking in various parameters that will
     * be recieved from a form submition. Not recommened for use for
     * simple testing purposes
     * 
     * @param questName
     * @param questDescription
     * @param completionCode
     * @param experienceRewarded
     * @param levelRequirement
     * @param aDateAvailable
     * @param aDateUnavailable
     * @param victoryText
     * @param prereqQuest
     * @param itemsRewardedPassed
     */
    public QuestEntity(String questName, String questDescription,
                                    String completionCode,
                                    int experienceRewarded,
                                    int levelRequirement,
                                    String aDateAvailable,
                                    String aDateUnavailable,
                                    String victoryText,
                                    String prereqQuest,
                                    List<Long> itemsRewardedPassed,
                                    int questPoints) {
        this.questName = questName;
        this.questDescription = questDescription;
        this.completionCode = completionCode;
        this.victoryText = victoryText;
        this.experienceRewarded = experienceRewarded;
        this.levelRequirement = levelRequirement;
        this.dateAvailable = aDateAvailable;
        this.dateUnavailable = aDateUnavailable;
        this.prereqQuest = prereqQuest;
        globallyAvailable = false;
        itemsRewarded = new ArrayList<Long>();
        itemsRewarded = itemsRewardedPassed;
        this.questPoints = questPoints;
        questCompleted = false;
        

    }

    /**
     * @return the completionCode
     */
    public String getCompletionCode() {
        return completionCode;
    }

    /**
     * @return the dateAvailable
     */
    public String getDateAvailable() {
        return dateAvailable;
    }

    /**
     * @return the dateUnavailable
     */
    public String getDateUnavailable() {
        return dateUnavailable;
    }

    /**
     * @return the experienceRewarded
     */
    public int getExperienceRewarded() {
        return experienceRewarded;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the itemsRewarded
     */
    public List<Long> getItemsRewarded() {
        return itemsRewarded;
    }

    /**
     * @return the levelRequirement
     */
    public int getLevelRequirement() {
        return levelRequirement;
    }

    /**
     * @return the prereqQuest
     */
    public String getPrereqQuest() {
        return prereqQuest;
    }

    /**
     * @return the questDescription
     */
    public String getQuestDescription() {
        return questDescription;
    }

    /**
     * @return the questName
     */
    public String getQuestName() {
        return questName;
    }

    /**
     * @return the victoryText
     */
    public String getVictoryText() {
        return victoryText;
    }

    /**
     * @return the globallyAvailable
     */
    public boolean isGloballyAvailable() {
        return globallyAvailable;
    }

    /**
     * @param aCompletionCode
     *            the completionCode to set
     */
    public void setCompletionCode(String aCompletionCode) {
        completionCode = aCompletionCode;
    }

    /**
     * @param aDateAvailable
     *            the dateAvailable to set
     */
    public void setDateAvailable(String aDateAvailable) {
        dateAvailable = aDateAvailable;
    }

    /**
     * @param aDateUnavailable
     *            the dateUnavailable to set
     */
    public void setDateUnavailable(String aDateUnavailable) {
        dateUnavailable = aDateUnavailable;
    }

    /**
     * @param aExperienceRewarded
     *            the experienceRewarded to set
     */
    public void setExperienceRewarded(int aExperienceRewarded) {
        experienceRewarded = aExperienceRewarded;
    }

    /**
     * @param aGloballyAvailable
     *            the globallyAvailable to set
     */
    public void setGloballyAvailable(boolean aGloballyAvailable) {
        globallyAvailable = aGloballyAvailable;
    }

    /**
     * @param aId
     *            the id to set
     */
    public void setId(Long aId) {
        id = aId;
    }

    /**
     * @param aItemsRewarded
     *            the itemsRewarded to set
     */
    public void setItemsRewarded(ArrayList<Long> aItemsRewarded) {
        itemsRewarded = aItemsRewarded;
    }

    /**
     * @param aLevelRequirement
     *            the levelRequirement to set
     */
    public void setLevelRequirement(int aLevelRequirement) {
        levelRequirement = aLevelRequirement;
    }

    /**
     * @param aPrereqQuest
     *            the prereqQuest to set
     */
    public void setPrereqQuest(String aPrereqQuest) {
        prereqQuest = aPrereqQuest;
    }

    /**
     * @param aQuestDescription
     *            the questDescription to set
     */
    public void setQuestDescription(String aQuestDescription) {
        questDescription = aQuestDescription;
    }

    /**
     * @param aQuestName
     *            the questName to set
     */
    public void setQuestName(String aQuestName) {
        questName = aQuestName;
    }

    /**
     * @param aVictoryText
     *            the victoryText to set
     */
    public void setVictoryText(String aVictoryText) {
        victoryText = aVictoryText;
    }
    
    public void completeQuest(){
    	questCompleted = true;
    }
    
    public boolean getQuestCompleted(){
    	return questCompleted;
    }
    
    public int getQuestPoints() {
  		return questPoints;
  	}

  	public void setQuestPoints(int questPoints) {
  		this.questPoints = questPoints;
  	}
}
