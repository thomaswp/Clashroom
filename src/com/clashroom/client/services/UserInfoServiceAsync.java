package com.clashroom.client.services;

import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.data.UserEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserInfoServiceAsync {
	public void getUser(AsyncCallback<UserEntity> callback);
	public void setUser(UserEntity user, AsyncCallback<Void> callback);
}
