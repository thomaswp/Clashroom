package com.clashroom.client.user;

import com.clashroom.client.FlowControl;
import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SetupPage extends Page {

	public final static String NAME = "Setup";

	private static UserInfoServiceAsync userInfoService =
			GWT.create(UserInfoService.class);
	
	private UserEntity user;
	
	public SetupPage(UserEntity user) {
		super(NAME);
		this.user = user;
		setupUI();
		setupUser();
	}
	
	public SetupPage() {
		this(NAME);
	}
	
	public SetupPage(String token) {
		super(token);
		setupUI();
		userInfoService.getUser(new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				user = result;
				setupUser();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	private SetupUser form;
	
	private void setupUI() {
		Window.setTitle("Setup");
	
		form = new SetupUser();
		form.addStyleName(NAME);
		initWidget(form);
	}
	
	private void setupUser() {
		if (user.isSetup()) {
			FlowControl.go(new HomePage());
			return;
		}
		form.setUser(user);
		form.setOnFinishedHandler(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		});
	}
	
	private void finish() {
		userInfoService.setUser(form.getUser(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				FlowControl.go(new HomePage());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
}
