package com.clashroom.shared;
import java.io.Serializable;

import com.clashroom.shared.entity.UserEntity;

/**
 * A class for holding information about a User's logged in state.
 */
@SuppressWarnings("serial")
public class LoginInfo implements Serializable {

	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private String nickname;
	private boolean hasAccount = false;
	private long userId;

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * The URL where the user can be redirected to log in
	 * @return The URL
	 */
	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	/**
	 * The URL where the user can be directed to log out
	 * @return The URL
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Returns true if this user has an account with Clashroom, meaning
	 * a {@link UserEntity} assigned to their email.
	 */
	public boolean hasAccount() {
		return hasAccount;
	}

	public void setHasAccount(boolean hasAccount) {
		this.hasAccount = hasAccount;
	}

	/**
	 * The id of the {@link UserEntity} assigned to this user.
	 * @return The id
	 */
	public long getUserId() {
		return userId;
	}

	public void setUserId(long id) {
		this.userId = id;
	}
}
