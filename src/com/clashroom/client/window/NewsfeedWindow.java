package com.clashroom.client.window;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.client.Clashroom;
import com.clashroom.client.Styles;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
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

public class NewsfeedWindow extends Composite {
	
	private final static DateTimeFormat dateFormat = 
			DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);
	
	private UserInfoServiceAsync userInfoService = GWT.create(UserInfoService.class);
	
	private FlexTable tableMe, tableEveryone;
	
	public NewsfeedWindow() {
		setupUI();
		
//		List<Long> users = new ArrayList<Long>();
//		users.add(Clashroom.getLoginInfo().getUserId());
		userInfoService.getNews(null, 5, new AsyncCallback<List<NewsfeedItem>>() {
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
		
		FlexTable outerMe = new FlexTable();
		outerMe.setText(0, 0, "When");
		outerMe.setText(0, 1, "What");
		outerMe.getColumnFormatter().addStyleName(1, Styles.text_right);
		outerMe.getRowFormatter().addStyleName(0, Styles.table_header);
		outerMe.getRowFormatter().addStyleName(0, Styles.gradient);
		outerMe.addStyleName(Styles.outer_table);
		outerMe.setCellSpacing(0);
		outerMe.setWidget(1, 0, scrollMe);
		outerMe.getFlexCellFormatter().setColSpan(1, 0, 2);
		

		tabPanel.add(outerMe, "Me");
		
		tableEveryone = new FlexTable();
		tableEveryone.addStyleName(Styles.table);
		tableEveryone.getRowFormatter().addStyleName(0,Styles.gradient);
		tableEveryone.getRowFormatter().addStyleName(0, Styles.table_header);
		
		ScrollPanel scrollEveryone = new ScrollPanel();
		scrollEveryone.setHeight("181px");
		scrollEveryone.add(tableEveryone);
		
		FlexTable outerEveryone = new FlexTable();
		outerEveryone.setText(0, 0, "When");
		outerEveryone.setText(0, 1, "What");
		outerEveryone.getColumnFormatter().addStyleName(1, Styles.text_right);
		outerEveryone.getRowFormatter().addStyleName(0, Styles.table_header);
		outerEveryone.getRowFormatter().addStyleName(0, Styles.gradient);
		outerEveryone.addStyleName(Styles.outer_table);
		outerEveryone.setCellSpacing(0);
		outerEveryone.setWidget(1, 0, scrollEveryone);
		outerEveryone.getFlexCellFormatter().setColSpan(1, 0, 2);
		
		tabPanel.add(outerEveryone, "Everyone");
		
		tabPanel.selectTab(0);
		
		initWidget(vPanel);
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
//		String[] headers = new String[] {
//				"When", "What"
//		};
//		
//		for (int i = 0; i < headers.length; i++) {
//			table.setText(0, i, headers[i]);
//		}
		
		int row = 1;
		for (NewsfeedItem item : news) {
			int col = 0;
			table.setText(row, col++, dateFormat.format(item.getDate()));
			table.setWidget(row, col++, item.getInfoWidget());
			row++;
		}
	}
}
