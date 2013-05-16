package com.clashroom.client.user;

import com.clashroom.client.FlowControl;
import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Page for setting up a new user. Most of the work in this
 * class is done in the {@link SetupPageUI} class.
 */
public class SetupPage extends Page {

	public final static String NAME = "Setup";

	private static UserInfoServiceAsync userInfoService = Services.userInfoService;
	
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
	
	private SetupPageUI form;
	
	private void setupUI() {
		Window.setTitle("Setup");
	
		form = new SetupPageUI();
		form.addStyleName(NAME);
		initWidget(form);
	}
	
	private void setupUser() {
		if (user.isSetup()) {
			//If the user is set up already, navigate them back home
			FlowControl.go(new HomePage());
			return;
		}
		form.setUser(user);
		//called when the UI handles the user clicking the final Next button
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
