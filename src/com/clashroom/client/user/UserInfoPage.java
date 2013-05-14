package com.clashroom.client.user;

import com.clashroom.client.Clashroom;
import com.clashroom.client.FlowControl;
import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.Debug;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserInfoPage extends Page {
	public final static String NAME = "UserInfo";
	
	private static UserInfoServiceAsync userInfoService = Services.userInfoService;
	
	private UserInfoPageUI userInfoWidget;
	private UserEntity user;
	
	public UserInfoPage() {
		this(NAME);
	}
	
	public UserInfoPage(String token) {
		super(token);
				
		setupUI();
		
		Long id = getLongParameter("id");
		
		AsyncCallback<UserEntity> callback = new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				if (!result.isSetup()) {
					FlowControl.go(new SetupPage(result));
				} else {
					user = result;
					if (user == null) {
						FlowControl.go(new HomePage());
						return;
					} else {
						populate();
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				FlowControl.go(new HomePage());
			}
		};
		
		if (id == null) {
			userInfoService.getUser(callback);	
		} else {
			userInfoService.getUser(id, callback);
		}
	}
	
	private void setupUI() {
		VerticalPanel panel = new VerticalPanel();
//		Hyperlink link = new Hyperlink("<", HomePage.NAME);
//		link.addStyleName(Styles.back_button);
//		panel.add(link);
		Window.setTitle("My Info");
		userInfoWidget = new UserInfoPageUI();
		userInfoWidget.setUserService(userInfoService);
		userInfoWidget.addStyleName(Styles.position_relative);
		userInfoWidget.setWidth("600px");
		panel.addStyleName(NAME);
		panel.add(userInfoWidget);
		initWidget(panel);
	}
	
	private void populate() {
		userInfoWidget.setUser(user, user.getId() == Clashroom.getLoginInfo().getUserId());
	}

}
