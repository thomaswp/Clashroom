package com.clashroom.shared.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

/**
 * An Entity representing a user playing Clashroom.
 * This entity contains the {@link DragonEntity} for the
 * user as well, which is embedded, meaning they are combined
 * into one entity in the datastore.
 */
@SuppressWarnings("serial")
@PersistenceCapable
public class UserEntity implements Serializable {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Long id;
	
	@Persistent
	private String username, firstName, lastName;
	
	@Persistent()
	private String email;
	
	//ids of ItemEntity's in this player's inventory
	@Persistent
	private List<Long> itemInventory = new ArrayList<Long>();
	
	//ids of QuestEntity's this player has compelted
	@Persistent
	private List<Long> completedQuests = new ArrayList<Long>();
	
	@Persistent(defaultFetchGroup="true")
	@Embedded
	private DragonEntity dragonEntity;
	
	//skill points to spend on learning new skills
	@Persistent
	private Integer skillPoints = 0;
	
	public boolean isSetup() {
		return id != null;
	}
	
	/**
	 * Gets the URL to this player's gravatar icon, using an identicon by default.
	 * This is a unique item, tied to the user's email. It is not currently used in Clashroom,
	 * but easily could be as a quick identifier for players on the newsfeed, etc.
	 */
	public String getIconUrl() {
		try {
			String hash = md5(getEmail());
			return "http://en.gravatar.com/avatar/" + hash + "?d=identicon";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//For hashing the user's email with md5 for getIconUrl()
	private static MessageDigest messageDigest;
	private String md5(String plaintext) throws NoSuchAlgorithmException {
		if (messageDigest == null) messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(plaintext.getBytes());
		byte[] digest = messageDigest.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);
		// Now we need to zero pad it if you actually want the full 32 chars.
		while(hashtext.length() < 32 ){
		  hashtext = "0"+hashtext;
		}
		return hashtext;
	}
	
	//Empty constructor necessary for passing this entity over RPC
	@Deprecated
	public UserEntity() { }
	
	public UserEntity(String email) {
		this.email = email;
		dragonEntity = new DragonEntity();
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DragonEntity getDragon() {
		return dragonEntity;
	}
	
	public void setDragon(DragonEntity dragon) {
		this.dragonEntity = dragon;
	}
	
	public Integer getSkillPoints() {
		if (skillPoints == null) return 0;
		return skillPoints;
	}
	
	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints; 
	}
	
	public void setItemsIventory(List<Long> listOfItems){
		itemInventory = listOfItems;
	}
	
	public void setCompletedQuests(List<Long> listOfCompletedQuests){
		completedQuests = listOfCompletedQuests;
	}
	
	public List<Long> getCompletedQuests(){
		return completedQuests;
	}
	public List<Long> getItemInventory(){
		return itemInventory;
	}
	public void addCompletedQuest(Long questId){
		completedQuests.add(questId);
	}
	public void addItemToInventory(Long itemId){
		itemInventory.add(itemId);
	}
}
