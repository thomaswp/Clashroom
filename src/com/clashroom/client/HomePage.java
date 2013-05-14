package com.clashroom.client;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.client.services.Services;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.client.window.IWindow;
import com.clashroom.client.window.ListBattleWindow;
import com.clashroom.client.window.NewsfeedWindow;
import com.clashroom.client.window.QuestsWindow;
import com.clashroom.client.window.TasksWindow;
import com.clashroom.client.window.UserInfoWindow;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class HomePage extends Page {
	
	public final static String NAME = "home";

	private List<IWindow> windows = new ArrayList<IWindow>();
	
	public HomePage() {
		this(NAME);
	}
	
	public HomePage(UserEntity user) {
		super(NAME);
		setup();
		userCallback(user);
	}
	
	public HomePage(String token) {
		super(token);
		setup();
		Services.userInfoService.getUser(new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				userCallback(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	private void userCallback(UserEntity user) {
		for (IWindow window : windows) window.onReceiveUserInfo(user);
	}
	
	private void setup() {		
		AbsolutePanel main = new AbsolutePanel();
		main.addStyleName(NAME);
		
		VerticalPanel box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("side");
		final UserInfoWindow userInfoWindow = new UserInfoWindow();
		windows.add(userInfoWindow);
		box.add(userInfoWindow);
		box.setWidth("24%");
		box.setHeight("690px");
		main.add(box);
		
		box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("center");
		box.setWidth("50%");
		QuestsWindow questWindow = new QuestsWindow();
		windows.add(questWindow);
		box.add(questWindow);
		ListBattleWindow listBattleWindow = new ListBattleWindow();
		windows.add(listBattleWindow);
		box.add(listBattleWindow);
		main.add(box);
		
		box = new VerticalPanel();
		box.addStyleName("column");
		box.addStyleName("side");
		
		NewsfeedWindow newsfeedWindow = new NewsfeedWindow();
		windows.add(newsfeedWindow);
		box.add(newsfeedWindow);
		
		TasksWindow tasksWindow = new TasksWindow();
		windows.add(tasksWindow);
		tasksWindow.setOnQuestCompletedListener(new Runnable() {
			@Override
			public void run() {
				userInfoWindow.update();
			}
		});
		box.add(tasksWindow);
		box.setWidth("24%");
		main.add(box);
		
		initWidget(main);
	}

}
