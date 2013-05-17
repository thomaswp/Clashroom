package com.clashroom.server.impl;

import javax.jdo.PersistenceManager;
import com.clashroom.client.services.LoginService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.LoginInfo;
import com.clashroom.shared.entity.UserEntity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * An RPC service for handling logging in/out of a user's account.
 */
@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
LoginService {

	/**
	 * Return information about the users login state. This includes
	 * whether or not the current user is logged in, if not where to
	 * redirect to log in, and if so where to log out.
	 * @param requestUri The URI to which to redirect the user after logging in/out.
	 * This is usually the current URL
	 */
	@Override
	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			UserEntity entity = QueryUtils.queryUnique(pm, UserEntity.class, "email==%s", user.getEmail());
			loginInfo.setHasAccount(entity != null);
			if (entity != null) {
				loginInfo.setUserId(entity.getId());
			}
			pm.close();
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

}
