package com.clashroom.client.services;

import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserInfoServiceAsync {
	public void getUser(AsyncCallback<UserEntity> callback);
	public void setUser(UserEntity user, AsyncCallback<Void> callback);
	public void addExp(int exp, AsyncCallback<Void> callback);
}
