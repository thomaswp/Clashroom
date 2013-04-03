package com.clashroom.client.page;

import com.clashroom.client.Clashroom;
import com.clashroom.client.FlowControl;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.data.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

public class UserInfoPage extends Page {
	public final static String NAME = "UserInfo";
	
	private static UserInfoServiceAsync userInfoService = 
			GWT.create(UserInfoService.class);
	
	public UserInfoPage() {
		this(NAME);
	}
	
	public UserInfoPage(String token) {
		super(token);
		
		Window.setTitle("My Info");
		
		initWidget(new Label());
		
		userInfoService.getUser(new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				if (!result.isSetup()) {
					FlowControl.go(new SetupPage(result));
				} else {
					
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

}
