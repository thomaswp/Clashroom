package com.clashroom.server.impl;

import javax.jdo.PersistenceManager;
import javax.persistence.Persistence;

import com.clashroom.client.services.LoginService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.LoginInfo;
import com.clashroom.shared.entity.UserEntity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
LoginService {

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
