package com.clashroom.client.window;

import java.util.ArrayList;
import com.clashroom.client.Styles;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.Debug;
import com.clashroom.shared.entity.QuestEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 * Even though this is named a window it isn't a true window,
 * purposely does not inherit functionality of Window.java
 * This Window is meant to be used on the main page
 */

public class QuestsWindow extends Composite implements IWindow {

	public final static String NAME = "studentQuests";

	private static QuestRetrieverServiceAsync questRetrieverSvc = Services.questRetrieverService;

	private ArrayList<QuestEntity> availableQuests;
	private UserEntity currentUser;
	private FlexTable studentQuests;

	public QuestsWindow(){

		// Set up the callback object.
		AsyncCallback<ArrayList<QuestEntity>> questCallback = new
				AsyncCallback<ArrayList<QuestEntity>>() {

			@Override public void onFailure(Throwable caught) {
				System.err.println("Error: RPC Call Failed");
				caught.printStackTrace(); 
			}

			@Override public void onSuccess(ArrayList<QuestEntity>result) 
			{ 
				availableQuests = result;
				listStudentQuests();
			} 
		};

		setUpUI(); 

		questRetrieverSvc.retrieveQuests(questCallback);        
	}

	public void listStudentQuests() {
		if (currentUser == null || availableQuests == null) {
			return;
		}
		
		int row = 1;
		for (int i = 0; i < availableQuests.size(); i++) 
		{
			if(!currentUser.getCompletedQuests().contains(availableQuests.get(i).getId())){
				studentQuests.setWidget(row++, 0, new QuestInnerWindow(availableQuests.get(i)));
				studentQuests.getFlexCellFormatter().setColSpan(i+1, 0, 4);
			} else {
				Debug.write("completed");
			}
		}

	}
	public String getName(){
		return NAME;
	}

	public void setUpUI(){
		VerticalPanel main = new VerticalPanel();
		Label label = new Label("Adventures");
		label.addStyleName(Styles.text_title);
		main.add(label);
		studentQuests = new FlexTable();
		studentQuests.addStyleName(Styles.table);
		main.addStyleName(NAME);
		studentQuests.getRowFormatter().addStyleName(0, Styles.table_header);
		studentQuests.getRowFormatter().addStyleName(0, Styles.gradient);
		studentQuests.setCellSpacing(0);
		//studentQuests.setWidth("100%");
		
		ScrollPanel scroll = new ScrollPanel();
		scroll.setHeight("225px");
		scroll.add(studentQuests);
		
		FlexTable outer = new FlexTable();
		String[] headers = new String[] {
				"Quest Name", "Description", "Reward", "Type"
		};
		
		for (int i = 0; i < headers.length; i++) {
			outer.setText(0, i, headers[i]);
			if (i == 0) outer.getColumnFormatter().setWidth(i, "20%");
			else if (i==1) outer.getColumnFormatter().setWidth(i, "50%");
			else if (i==2) outer.getColumnFormatter().setWidth(i, "10%");
			else if (i==3) outer.getColumnFormatter().setWidth(i, "25%");
		}
		outer.getColumnFormatter().addStyleName(1, Styles.text_right);
		outer.getRowFormatter().addStyleName(0, Styles.table_header);
		outer.getRowFormatter().addStyleName(0, Styles.gradient);
		outer.addStyleName(Styles.outer_table);
		outer.setCellSpacing(0);
		outer.setWidget(1, 0, scroll);
		outer.getFlexCellFormatter().setColSpan(1, 0, 4);
		
		main.add(outer);
		initWidget(main);
	}

	@Override
	public void onReceiveUserInfo(UserEntity user) {
		currentUser = user;
		listStudentQuests();
	}
}

