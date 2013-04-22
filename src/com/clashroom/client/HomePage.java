package com.clashroom.client;

import com.clashroom.client.battle.ListBattlePage;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.client.window.QuestsWindow;
import com.clashroom.client.window.TasksWindow;
import com.clashroom.client.window.UserInfoWindow;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
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
		box.add(new UserInfoWindow());
		box.setWidth("24%");
		main.add(box);
		
		box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("center");
		box.setWidth("50%");
		QuestsWindow qw = new QuestsWindow();
		box.add(qw);
		link = new Hyperlink("Arena Listings", ListBattlePage.NAME);
		link.addStyleName(Styles.text_title);
		//box.add(link);
		box.add(new ListBattlePage());
		main.add(box);
		
		box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("side");
		link = new Hyperlink("Town Herald", "");
		link.addStyleName(Styles.text_title);
		box.add(link);
		
		TasksWindow sqw = new TasksWindow();
		box.add(sqw);
		box.setWidth("24%");
		main.add(box);
		
		initWidget(main);
	}

}
