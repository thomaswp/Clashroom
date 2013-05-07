package com.clashroom.client.window;

import java.util.ArrayList;
import com.clashroom.client.Styles;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
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
	   } };
	    
	   	setUpUI(); 

	   	userRetrieverSvc.getUser(callBack);
	    questRetrieverSvc.retrieveQuests(callback);        
	}
	
	public void listStudentQuests() {
		/*
		 * Will be taken out later. Meant to just check if any quests
		 * exists and if they don't create one and slap it in.
		 */
		if(availableQuests.size() < 1){
			createDummyQuest();
		}        
        
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
		studentQuests.addStyleName(Styles.table);
		main.addStyleName(NAME);
		studentQuests.getRowFormatter().addStyleName(0, Styles.table_header);
		studentQuests.getRowFormatter().addStyleName(0, Styles.gradient);
		studentQuests.setCellSpacing(0);
		main.add(studentQuests);
		initWidget(main);
	}
	
	/*
	 * For testing purposes, will eventually delete
	 */
	private void createDummyQuest(){
		
		if (questRetrieverSvc == null) 
		   { 
		     questRetrieverSvc = GWT.create(QuestRetrieverService.class); 
		   }
		          
		   // Set up the callback object.
		   AsyncCallback<String> callback = new
		   AsyncCallback<String>() {
		          
		   @Override 
		   public void onFailure(Throwable caught) {
		   System.err.println("Error: RPC Call Failed");
		   	caught.printStackTrace(); 
		   }
		          
		   @Override 
		   public void onSuccess(String result) 
		   { 
			   
		   } };
		    
		    questRetrieverSvc.addDummyQuest(callback); 	
	}

	private void createItems(){
		
		if (itemCreatorSvc == null) 
		   { 
		     itemCreatorSvc = GWT.create(ItemRetrieverService.class); 
		   }
		          
		   // Set up the callback object.
		   AsyncCallback<Void> callback = new
		   AsyncCallback<Void>() {
		          
		   @Override 
		   public void onFailure(Throwable caught) {
		   System.err.println("Error: RPC Call Failed");
		   	caught.printStackTrace(); 
		   }
		          
		   @Override 
		   public void onSuccess(Void result) 
		   { 
			   
		   } };
		    
		    itemCreatorSvc.addItems(callback); 	
	}
}

