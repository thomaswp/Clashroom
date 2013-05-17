package com.clashroom.client.services;

import com.google.gwt.core.shared.GWT;

/**
 * A static class to store RPC services so they
 * only need by instantiated once.
 */
public class Services {
	public final static QuestRetrieverServiceAsync questRetrieverService = 
			GWT.create(QuestRetrieverService.class);

	public final static UserInfoServiceAsync userInfoService = 
			GWT.create(UserInfoService.class);

	public final static ItemRetrieverServiceAsync itemRetrieverService = 
			GWT.create(ItemRetrieverService.class);
	
	public final static BattleServiceAsync battleService = 
			GWT.create(BattleService.class);
	
	public final static LoginServiceAsync loginService = 
			GWT.create(LoginService.class);
	
	public final static TaskServiceAsync taskService =
			GWT.create(TaskService.class);
}
