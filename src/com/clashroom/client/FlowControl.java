package com.clashroom.client;

import com.clashroom.client.page.BattlePage;
import com.clashroom.client.page.Page;
import com.clashroom.client.page.SelectBattlePage;
import com.clashroom.shared.Debug;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class FlowControl {

	private static Page oldPage; 
	
	public static void go(Page c) {
			
		RootPanel root = RootPanel.get("main");
		root.clear();
		
		if (oldPage != null) {
			oldPage.cleanup();
		}
		oldPage = c;
		
		root.getElement().getStyle().setPosition(Position.RELATIVE); // not sure why, but GWT throws an exception without this. Adding to CSS doesn't work.
		// add, determine height/width, center, then move. height/width are unknown until added to document. Catch-22!
		root.add(c);
		int left = Window.getClientWidth() / 2 - c.getOffsetWidth() / 2; // find center
		int top = 100; //Window.getClientHeight() / 2 - c.getOffsetHeight() / 2;
		root.setWidgetPosition(c, left, top);
		History.newItem(History.encodeHistoryToken(c.getToken()));
	}

	public static void go(String token) {
		if (token == null) return;
		
		if (token.startsWith(BattlePage.NAME)) {
			go(new BattlePage(token));
		} else if (token.startsWith(SelectBattlePage.NAME)) {
			go(new SelectBattlePage(token));
		}
		
	}
}
