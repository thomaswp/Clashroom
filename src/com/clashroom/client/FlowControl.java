package com.clashroom.client;

import java.util.LinkedList;

import com.clashroom.client.page.BattlePage;
import com.clashroom.client.page.Page;
import com.clashroom.client.page.SelectBattlePage;
import com.clashroom.client.page.SetupPage;
import com.clashroom.client.page.UserInfoPage;
import com.clashroom.shared.Debug;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class FlowControl {

	private static Page oldPage;
	
	private static LinkedList<OnPageChangedListener> listeners =
			new LinkedList<OnPageChangedListener>();
	
	public static void addOnPageChangedListener(OnPageChangedListener listener) {
		listeners.add(listener);
	}
	
	public static void go(Page nextPage) {
			
		RootPanel root = RootPanel.get("main");
		root.clear();
		
		if (oldPage != null) {
			oldPage.cleanup();
		}
		oldPage = nextPage;
		
		root.getElement().getStyle().setPosition(Position.RELATIVE); // not sure why, but GWT throws an exception without this. Adding to CSS doesn't work.
		// add, determine height/width, center, then move. height/width are unknown until added to document. Catch-22!
		root.add(nextPage);
		int left = Window.getClientWidth() / 2 - nextPage.getOffsetWidth() / 2; // find center
		int top = 100; //Window.getClientHeight() / 2 - c.getOffsetHeight() / 2;
		root.setWidgetPosition(nextPage, left, top);
		Debug.write(nextPage.getToken());
		History.newItem(History.encodeHistoryToken(nextPage.getToken()));
		
		for (OnPageChangedListener listener : listeners) {
			listener.onPageChanged(nextPage);
		}
	}

	public static void go(String token) {
		if (token == null) return;
		
		if (token.startsWith(BattlePage.NAME)) {
			go(new BattlePage(token));
		} else if (token.startsWith(SelectBattlePage.NAME)) {
			go(new SelectBattlePage(token));
		} else if (token.startsWith(SetupPage.NAME)) {
			go(new SetupPage(token));
		} else if (token.startsWith(UserInfoPage.NAME)) {
			go(new UserInfoPage(token));
		}
	}
	
	public interface OnPageChangedListener {
		void onPageChanged(Page nextPage);
	}
}
