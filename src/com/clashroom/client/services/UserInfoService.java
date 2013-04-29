package com.clashroom.client.services;

import java.util.List;

import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserInfoService extends RemoteService {
	public UserEntity getUser();
	public void setUser(UserEntity user);
	public List<UserEntity> getAllUsers();
}
