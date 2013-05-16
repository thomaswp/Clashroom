package com.clashroom.client;

import java.util.LinkedList;

import com.clashroom.client.battle.BattlePage;
import com.clashroom.client.battle.BattlePrepPage;
import com.clashroom.client.bounty.BountiesPage;
import com.clashroom.client.quest.ItemDetailsPage;
import com.clashroom.client.quest.QuestDetailPage;
import com.clashroom.client.teacher.CreateBattlePage;
import com.clashroom.client.teacher.CreateQuestPage;
import com.clashroom.client.teacher.CreatedQuestsPage;
import com.clashroom.client.user.SetupPage;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.Debug;
import com.clashroom.shared.entity.BattleEntity;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A static class to control navigation between {@link Page}s.
 * To navigate to a Page, use {@link #go(Page)}, passing a 
 * Page object, or use {@link #go(String)} and pass the appropriate
 * token.
 *
 */
public class FlowControl {

	private static Page oldPage;
	
	private static LinkedList<OnPageChangedListener> listeners =
			new LinkedList<OnPageChangedListener>();
	
	public static void addOnPageChangedListener(OnPageChangedListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Navigates to a {@link Page}.
	 * @param nextPage The page to which to navigate
	 */
	public static void go(Page nextPage) {
			
		RootPanel root = RootPanel.get("main");
		root.clear();
		
		if (oldPage != null) {
			oldPage.cleanup();
		}
		oldPage = nextPage;
		
		root.add(nextPage);
		Debug.write("Visiting page: " + nextPage.getToken());
		History.newItem(History.encodeHistoryToken(nextPage.getToken()));
		
		for (OnPageChangedListener listener : listeners) {
			listener.onPageChanged(nextPage);
		}
	}

	/**
	 * Creates a {@link Page} based on the token passed. If not 
	 * corresponding page is found, the {@link History#back()} method is called.
	 * A proper token consists of a Page's NAME field, with optional
	 * http-style parameters. For example, passing "Battle?id=1" would
	 * load the {@link BattlePage} and load the {@link BattleEntity} with
	 * an id of 1.
	 * @param token The page token to load
	 */
	public static void go(String token) {
		if (token == null) return;
		
		if (token.startsWith(BattlePage.NAME)) {
			go(new BattlePage(token));
		} else if (token.startsWith(SetupPage.NAME)) {
			go(new SetupPage(token));
		} else if (token.startsWith(UserInfoPage.NAME)) {
			go(new UserInfoPage(token));
		} else if (token.startsWith(HomePage.NAME)) {
			go(new HomePage(token));
		} else if (token.startsWith(BountiesPage.NAME)) {
			go(new BountiesPage(token));
		} else if(token.startsWith(CreatedQuestsPage.NAME)){
        	go(new CreatedQuestsPage(token));
        } else if(token.startsWith(QuestDetailPage.NAME)){
        	go(new QuestDetailPage(token));
        } else if (token.startsWith(CreateQuestPage.NAME)){
        	go(new CreateQuestPage(token));
        } else if (token.startsWith(CreateBattlePage.NAME)){
        	go(new CreateBattlePage(token));
        } else if (token.startsWith(BattlePrepPage.NAME)){
        	go(new BattlePrepPage(token));
        } else if (token.startsWith(ItemDetailsPage.NAME)){
        	go(new ItemDetailsPage(token));
        } else {
			History.back();
			throw new RuntimeException("No such page " + token);
        }
	}
	
	public interface OnPageChangedListener {
		void onPageChanged(Page nextPage);
	}
}
