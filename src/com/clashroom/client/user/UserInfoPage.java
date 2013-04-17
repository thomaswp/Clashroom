package com.clashroom.client.user;

import com.clashroom.client.FlowControl;
import com.clashroom.client.page.Page;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserInfoPage extends Page {
	public final static String NAME = "UserInfo";
	
	private static UserInfoServiceAsync userInfoService = 
			GWT.create(UserInfoService.class);
	
	private UserInfo userInfoWidget;
	private UserEntity user;
	
	public UserInfoPage() {
		this(NAME);
	}
	
	public UserInfoPage(String token) {
		super(token);
				
		setupUI();
		
		userInfoService.getUser(new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				if (!result.isSetup()) {
					FlowControl.go(new SetupPage(result));
				} else {
					user = result;
					populate();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	private void setupUI() {
		Window.setTitle("My Info");
		userInfoWidget = new UserInfo();
		initWidget(userInfoWidget);
	}
	
	private void populate() {
		userInfoWidget.setUser(user);
	}

}
