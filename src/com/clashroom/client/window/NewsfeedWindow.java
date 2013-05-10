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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewsfeedWindow extends Composite {
	
	private final static DateTimeFormat dateFormat = 
			DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);
	
	private UserInfoServiceAsync userInfoService = GWT.create(UserInfoService.class);
	
	private FlexTable table;
	
	public NewsfeedWindow() {
		setupUI();
		
		List<Long> users = new ArrayList<Long>();
		users.add(Clashroom.getLoginInfo().getUserId());
		userInfoService.getNews(users, 5, new AsyncCallback<List<NewsfeedItem>>() {
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
		
		Label title = new Label("Town Herald");
		title.addStyleName(Styles.text_title);
		vPanel.add(title);
		
		table = new FlexTable();
		table.addStyleName(Styles.table);
		table.getRowFormatter().addStyleName(0,Styles.gradient);
		table.getRowFormatter().addStyleName(0, Styles.table_header);
		
		ScrollPanel scroll = new ScrollPanel();
		scroll.setHeight("175px");
		scroll.add(table);
		
		FlexTable outer = new FlexTable();
		outer.setText(0, 0, "When");
		outer.setText(0, 1, "What");
		outer.getColumnFormatter().addStyleName(1, Styles.text_right);
		outer.getRowFormatter().addStyleName(0, Styles.table_header);
		outer.getRowFormatter().addStyleName(0, Styles.gradient);
		outer.addStyleName(Styles.outer_table);
		outer.setCellSpacing(0);
		outer.setWidget(1, 0, scroll);
		outer.getFlexCellFormatter().setColSpan(1, 0, 2);
		
		vPanel.add(outer);
		
		initWidget(vPanel);
	}
	
	private void populateUI(List<NewsfeedItem> result) {
//		String[] headers = new String[] {
//				"When", "What"
//		};
//		
//		for (int i = 0; i < headers.length; i++) {
//			table.setText(0, i, headers[i]);
//		}
		
		int row = 1;
		for (NewsfeedItem item : result) {
			int col = 0;
			table.setText(row, col++, dateFormat.format(item.getDate()));
			table.setWidget(row, col++, item.getInfoWidget());
			row++;
		}
	}
}
