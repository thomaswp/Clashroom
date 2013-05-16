package com.clashroom.client.window;

import com.clashroom.client.HomePage;
import com.clashroom.shared.entity.UserEntity;

/**
 * Indicates this Window on the {@link HomePage} would like
 * to receive the {@link UserEntity} when it is received from the
 * server.
 */
public interface IWindow {
	/**
	 * Called when the {@link UserEntity} is received from
	 * the server.
	 * @param user The UserEntity
	 */
	void onReceiveUserInfo(UserEntity user); 
}
