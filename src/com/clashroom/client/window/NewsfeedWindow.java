package com.clashroom.client.window;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.client.Clashroom;
import com.clashroom.client.Styles;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
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

public class NewsfeedWindow extends Composite implements IWindow {
	
	private final static DateTimeFormat dateFormat = 
			DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);
	
	private UserInfoServiceAsync userInfoService = Services.userInfoService;
	
	private FlexTable tableMe, tableEveryone;
	
	public NewsfeedWindow() {
		setupUI();
		update();
	}
	
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
	
	private void setupUI() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setWidth("100%");
		vPanel.addStyleName("newsfeed");
		
		Label title = new Label("Town Herald");
		title.addStyleName(Styles.text_title);
		vPanel.add(title);
		
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setWidth("100%");
		vPanel.add(tabPanel);
		
		tableMe = new FlexTable();
		tableMe.addStyleName(Styles.table);
		tableMe.getRowFormatter().addStyleName(0,Styles.gradient);
		tableMe.getRowFormatter().addStyleName(0, Styles.table_header);
		
		ScrollPanel scrollMe = new ScrollPanel();
		scrollMe.setHeight("181px");
		scrollMe.add(tableMe);	

		tabPanel.add(getScrollingTable(tableMe), "Me");
		
		tableEveryone = new FlexTable();
		tableEveryone.addStyleName(Styles.table);
		tableEveryone.getRowFormatter().addStyleName(0,Styles.gradient);
		tableEveryone.getRowFormatter().addStyleName(0, Styles.table_header);
		
		
		tabPanel.add(getScrollingTable(tableEveryone), "Everyone");
		
		tabPanel.selectTab(0);
		
		initWidget(vPanel);
	}
	
	private FlexTable getScrollingTable(FlexTable table) {
		ScrollPanel scroll = new ScrollPanel();
		scroll.setHeight("181px");
		scroll.add(table);
		
		FlexTable outer = new FlexTable();
		outer.setText(0, 0, "When");
		outer.setText(0, 1, "What");
		outer.getColumnFormatter().addStyleName(1, Styles.text_right);
		outer.getColumnFormatter().setWidth(0, "95px");
		outer.getRowFormatter().addStyleName(0, Styles.table_header);
		outer.getRowFormatter().addStyleName(0, Styles.gradient);
		outer.addStyleName(Styles.outer_table);
		outer.setCellSpacing(0);
		outer.setWidget(1, 0, scroll);
		outer.getFlexCellFormatter().setColSpan(1, 0, 2);
		return outer;
	}
	
	private void populateUI(List<NewsfeedItem> result) {
		populateTable(tableEveryone, result);
		ArrayList<NewsfeedItem> myNews = new ArrayList<NewsfeedItem>();
		for (NewsfeedItem item : result) {
			if (item.getPlayerIds().contains(Clashroom.getLoginInfo().getUserId())) {
				myNews.add(item);
			}
		}
		populateTable(tableMe, myNews);
	}
	
	private void populateTable(FlexTable table, List<NewsfeedItem> news) {
		table.clear();
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
		
	}
}
