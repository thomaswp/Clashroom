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
import com.clashroom.client.widget.ScrollableFlexTable;
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
	private ScrollableFlexTable studentQuests;

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
				studentQuests.setWidget(row, 0, new QuestInnerWindow(availableQuests.get(i)));
				studentQuests.getInnerTable().getFlexCellFormatter().setColSpan(row, 0, 4);
				row++;
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
		main.addStyleName(NAME);

		studentQuests = new ScrollableFlexTable();
		studentQuests.setHeaders("Quest Name", "Description", "Reward", "Type");
		studentQuests.setHeaderWidths("20%", "50%", "10%", "25%");
		studentQuests.setColumnWidths("20%", "50%", "10%", "25%");
		studentQuests.addHeaderStyles(Styles.table_header, Styles.gradient);
		studentQuests.getInnerTable().addStyleName(Styles.table);
		studentQuests.getOuterTable().addStyleName(Styles.outer_table);
		studentQuests.getScrollPanel().setHeight("225px");
		studentQuests.getInnerTable().setCellSpacing(0);
		
		main.add(studentQuests);
		initWidget(main);
	}

	@Override
	public void onReceiveUserInfo(UserEntity user) {
		currentUser = user;
		listStudentQuests();
	}
}

