package com.clashroom.client.services;

import java.util.List;

import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserInfoServiceAsync {
	public void getUser(AsyncCallback<UserEntity> callback);
	public void setUser(UserEntity user, AsyncCallback<Void> callback);
	public void getAllUsers(AsyncCallback<List<UserEntity>> callback);
	public void learnSkill(int id, AsyncCallback<Void> callback);
}
