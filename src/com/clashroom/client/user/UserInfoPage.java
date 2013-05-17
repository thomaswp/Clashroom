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
import com.clashroom.shared.Formatter;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * {@link Page} for viewing a user's basic information, as well
 * as learning skill when viewing your own page. Most of the UI
 * for this page is implemented in {@link UserInfoPageUI}.
 */
public class UserInfoPage extends Page {
	public final static String NAME = "UserInfo";
	
	private static UserInfoServiceAsync userInfoService = Services.userInfoService;
	
	private UserInfoPageUI userInfoWidget;
	private UserEntity user;
	
	/**
	 * Returns HTML for a link to this page for viewing
	 * the user with the given id.
	 * @param text The text to go inside the anchor tags
	 * @param id The id of the user
	 * @return The HTML for the link
	 */
	public static String getHTMLLinkToUser(String text, long id) {
		return Formatter.format("<a href=#%s?id=%s>%s</a>",
				NAME, id, text);
	}
	
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
					//If this user isn't setup, redirect to the setup page
					FlowControl.go(new SetupPage(result));
				} else {
					user = result;
					if (user == null) {
						//If this user doesn't exist (meaning you provided an
						//invalid id), redirect to the home page
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
			//If there's no id parameter, use the logged in user
			userInfoService.getUser(callback);	
		} else {
			//Otherwise use the provided id
			userInfoService.getUser(id, callback);
		}
	}
	
	private void setupUI() {
		VerticalPanel panel = new VerticalPanel();
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
