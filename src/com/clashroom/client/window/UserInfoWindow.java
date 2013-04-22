package com.clashroom.client.window;

import com.clashroom.client.FlowControl;
import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserInfoWindow extends Window {
	
	private static UserInfoServiceAsync userInfoService = 
			GWT.create(UserInfoService.class);
	
	private UserEntity user;
	private Image image;
	private Label labelDragonName;
	
	public UserInfoWindow() {
		super();
		setupUI();
		
		userInfoService.getUser(new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				user = result;
				populate();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	private void setupUI() {
		VerticalPanel panel = new VerticalPanel();
//		Hyperlink link = new Hyperlink("<", HomePage.NAME);
//		link.addStyleName(Styles.back_button);
//		panel.add(link);

		//panel.addStyleName(UserInfoPage.NAME);
		
		labelDragonName = new Label();
		labelDragonName.addStyleName(Styles.text_title);
		panel.add(labelDragonName);
		
		SimplePanel simple = new SimplePanel();
		simple.addStyleName(Styles.profile_portrait);
		image = new Image();
		simple.add(image);
		panel.add(simple);
		
		focusPanel.add(panel);
		initWidget(focusPanel);
	}
	
	private void populate() {
		image.setUrl("img/" + user.getDragon().getDragonClass().getImageName());
		labelDragonName.setText(user.getDragon().getName());
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void click() {
		FlowControl.go(new UserInfoPage());
	}

}
