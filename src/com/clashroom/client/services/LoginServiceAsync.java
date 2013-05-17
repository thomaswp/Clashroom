package com.clashroom.client.services;

import com.clashroom.server.impl.LoginServiceImpl;
import com.clashroom.shared.LoginInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * See {@link LoginServiceImpl}
 */
public interface LoginServiceAsync {
	/** See {@link LoginServiceImpl#login(String)} */
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
}
