package com.clashroom.client;

import com.clashroom.client.user.UserInfoPage;
import com.clashroom.client.window.ListBattleWindow;
import com.clashroom.client.window.NewsfeedWindow;
import com.clashroom.client.window.QuestsWindow;
import com.clashroom.client.window.TasksWindow;
import com.clashroom.client.window.UserInfoWindow;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class HomePage extends Page {
	
	public final static String NAME = "home";

	public HomePage() {
		this(NAME);
	}
	
	public HomePage(String token) {
		super(token);
		
		setup();
	}
	
	public void setup() {		
		AbsolutePanel main = new AbsolutePanel();
		main.addStyleName(NAME);
		
		VerticalPanel box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("side");
		Hyperlink link = new Hyperlink("Profile", UserInfoPage.NAME);
		link.addStyleName(Styles.text_title);
		//box.add(link);
		final UserInfoWindow userInfoWindow = new UserInfoWindow();
		box.add(userInfoWindow);
		box.setWidth("24%");
		box.setHeight("690px");
		main.add(box);
		
		box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("center");
		box.setWidth("50%");
		QuestsWindow qw = new QuestsWindow();
		box.add(qw);
		link = new Hyperlink("Arena Listings", ListBattleWindow.NAME);
		link.addStyleName(Styles.text_title);
		//box.add(link);
		box.add(new ListBattleWindow());
		main.add(box);
		
		box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("side");
		
		box.add(new NewsfeedWindow());
		
		TasksWindow sqw = new TasksWindow();
		sqw.setOnQuestCompletedListener(new Runnable() {
			@Override
			public void run() {
				userInfoWindow.update();
			}
		});
		box.add(sqw);
		box.setWidth("24%");
		main.add(box);
		
		initWidget(main);
	}

}
