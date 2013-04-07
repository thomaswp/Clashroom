package com.clashroom.client.services;

import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.data.UserEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserInfoService extends RemoteService {
	public UserEntity getUser();
	public void setUser(UserEntity user);
}
