package com.clashroom.client.page;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.clashroom.client.fragments.SetupUser;
import com.clashroom.client.resources.MyResources;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.Debug;
import com.clashroom.shared.data.UserEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

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
		initWidget(form);
		
//		HTML html = new HTML();
//		html.setHTML(MyResources.INSTANCE.getIntroHtml().getText());
//		initWidget(html);
		
	}
	
	private void setupUser() {
//		DOM.getElementById("email").setInnerHTML(user.getEmail());
		form.getLabelEmail().setText(user.getEmail());
		form.getImageIcon().setUrl(user.getIconUrl());
	}
	
	
}
