package com.clashroom.shared.data;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;

import com.clashroom.server.QueryUtils;

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
	
	@Persistent(defaultFetchGroup="true")
	@Embedded
	private DragonEntity dragonEntity;
	
	public boolean isSetup() {
		return id != null;
	}
	
	public String getIconUrl() {
		try {
			String hash = md5(getEmail());
			return "http://en.gravatar.com/avatar/" + hash + "?d=identicon";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
}
