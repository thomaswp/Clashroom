package com.clashroom.client.window;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.client.Clashroom;
import com.clashroom.client.HomePage;
import com.clashroom.client.Styles;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.widget.ScrollableFlexTable;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.news.NewsfeedItem;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Window which displays recent happenings in Clashroom to the user,
 * to be displayed on the {@link HomePage}.
 */
public class NewsfeedWindow extends Composite implements IWindow {
	
	private final static DateTimeFormat dateFormat = 
			DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);
	
	private UserInfoServiceAsync userInfoService = Services.userInfoService;
	
	private ScrollableFlexTable tableMe, tableEveryone;
	
	public NewsfeedWindow() {
		setupUI();
		update();
	}
	
	/**
	 * When called, this Window will re-query the server
	 * for news and redisplay it.
	 */
	public void update() {
		userInfoService.getNews(null, 20, new AsyncCallback<List<NewsfeedItem>>() {
			@Override
			public void onSuccess(List<NewsfeedItem> result) {
				populateUI(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	//Set up the UI
	private void setupUI() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		vPanel.addStyleName("newsfeed");
		
		Label title = new Label("Town Herald");
		title.addStyleName(Styles.text_title);
		vPanel.add(title);
		
		//Tab panel to switch between my news and all news
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setWidth("100%");
		vPanel.add(tabPanel);
		
		tableMe = getScrollingTable();
		tabPanel.add(tableMe, "Me");
		
		tableEveryone = getScrollingTable();
		tabPanel.add(tableEveryone, "Everyone");
		
		tabPanel.selectTab(0);
		
		initWidget(vPanel);
	}
	
	//Creates a ScrollingFlexTable
	private ScrollableFlexTable getScrollingTable() {
		ScrollableFlexTable table = new ScrollableFlexTable();
		table.setHeaders("When", "What");
		table.addHeaderStyles(Styles.table_header, Styles.gradient);
		table.setHeaderWidths("95px", "");
		table.getInnerTable().addStyleName(Styles.table);
		table.getOuterTable().addStyleName(Styles.outer_table);
		table.getScrollPanel().setHeight("181px");
		
		return table;
	}
	
	//populates the UI when news is received
	private void populateUI(List<NewsfeedItem> result) {
		populateTable(tableEveryone, result);
		ArrayList<NewsfeedItem> myNews = new ArrayList<NewsfeedItem>();
		for (NewsfeedItem item : result) {
			if (item.getPlayerIds().contains(Clashroom.getLoginInfo().getUserId())) {
				//if this news is about me, add it to tableMe
				myNews.add(item);
			}
		}
		populateTable(tableMe, myNews);
	}
	
	//populates a single table with a list of news
	private void populateTable(ScrollableFlexTable table, List<NewsfeedItem> news) {
		table.clearNonHeaders();
		int row = 1;
		for (NewsfeedItem item : news) {
			int col = 0;
			table.setText(row, col++, dateFormat.format(item.getDate()));
			table.setWidget(row, col++, item.getInfoWidget());
			row++;
		}
	}

	@Override
	public void onReceiveUserInfo(UserEntity user) {
		//We don't do anything with the UserEntity at present
	}
}
