package com.clashroom.client;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.client.services.Services;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.client.window.IWindow;
import com.clashroom.client.window.ListBattleWindow;
import com.clashroom.client.window.NewsfeedWindow;
import com.clashroom.client.window.QuestsWindow;
import com.clashroom.client.window.BountiesWindow;
import com.clashroom.client.window.UserInfoWindow;
import com.clashroom.client.window.Window;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The main page of the application and the home page for the user.
 * This page mostly contains the {@link Window}s that make it up.
 */
public class HomePage extends Page {
	
	public final static String NAME = "home";

	//IWindows will be passed the UserInfoEntity when it is retrieved from the server
	private List<IWindow> windows = new ArrayList<IWindow>();
	
	public HomePage() {
		this(NAME);
	}
	
	public HomePage(String token) {
		super(token);
		setupUI();
		//Fetch the UserEntity from the server and pass it on to the Windows
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
	
	//Formats the layout of the 5 Windows on the home screen
	private void setupUI() {		
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
		
		final NewsfeedWindow newsfeedWindow = new NewsfeedWindow();
		windows.add(newsfeedWindow);
		box.add(newsfeedWindow);
		
		BountiesWindow tasksWindow = new BountiesWindow();
		windows.add(tasksWindow);
		tasksWindow.setOnTaskCompletedListener(new Runnable() {
			@Override
			public void run() {
				//When a task is completed, we want to
				//update the UserInfoWindow and the NewsfeedWindow
				//to show the results
				userInfoWindow.update();
				newsfeedWindow.update();
			}
		});
		box.add(tasksWindow);
		box.setWidth("24%");
		main.add(box);
		
		//All Components (and therefore Pages) have to
		//call initWidget with their main widget.
		initWidget(main);
	}

}
