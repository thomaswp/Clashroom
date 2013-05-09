package com.clashroom.client.window;

import java.util.ArrayList;
import com.clashroom.client.Styles;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
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

public class QuestsWindow extends Composite {

	public final static String NAME = "studentQuests";

	private static QuestRetrieverServiceAsync questRetrieverSvc = GWT
			.create(QuestRetrieverService.class);

	private static UserInfoServiceAsync userRetrieverSvc = GWT
			.create(UserInfoService.class);

	private static ItemRetrieverServiceAsync itemCreatorSvc = GWT
			.create(ItemRetrieverService.class);

	private ArrayList<QuestEntity> availableQuests;
	private UserEntity currentUser;
	private FlexTable studentQuests;
	private ScrollPanel scrollPanel;

	public QuestsWindow(){

		if(userRetrieverSvc == null){
			userRetrieverSvc = GWT.create(UserInfoService.class);
		}

		if (questRetrieverSvc == null) 
		{ 
			questRetrieverSvc = GWT.create(QuestRetrieverService.class); 
		}




		AsyncCallback<UserEntity> callBack = new AsyncCallback<UserEntity>(){

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Error: RPC Call Failed");
				caught.printStackTrace();		
			}

			@Override
			public void onSuccess(UserEntity result) {
				currentUser = result;	
			}

		};

		// Set up the callback object.
		AsyncCallback<ArrayList<QuestEntity>> callback = new
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

		userRetrieverSvc.getUser(callBack);
		questRetrieverSvc.retrieveQuests(callback);        
	}

	public void listStudentQuests() {       

		String[] headers = new String[] {
				"Quest Name", "Description", "Reward", "Type"
		};

		for (int i = 0; i < headers.length; i++) {
			studentQuests.setText(0, i, headers[i]);
			if (i == 0) studentQuests.getColumnFormatter().setWidth(i, "25%");
			else if (i==1) studentQuests.getColumnFormatter().setWidth(i, "50%");
			else if (i==2) studentQuests.getColumnFormatter().setWidth(i, "12.5%");
			else if (i==3) studentQuests.getColumnFormatter().setWidth(i, "12.5%");
		}
		for (int i = 0; i < availableQuests.size(); i++) 
		{	
			if(!currentUser.getCompletedQuests().contains(availableQuests.get(i).getId())){
				studentQuests.setWidget(i + 1, 0, new QuestInnerWindow(availableQuests.get(i)));
				studentQuests.getFlexCellFormatter().setColSpan(i+1, 0, 4);
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
		scrollPanel = new ScrollPanel();
		studentQuests.addStyleName(Styles.table);
		main.addStyleName(NAME);
		studentQuests.getRowFormatter().addStyleName(0, Styles.table_header);
		studentQuests.getRowFormatter().addStyleName(0, Styles.gradient);
		studentQuests.setCellSpacing(0);
		scrollPanel.add(studentQuests);
		scrollPanel.setSize("100", "100");//Not working ask Dan about making it scrollable
		main.add(scrollPanel);
		initWidget(main);
	}
}

