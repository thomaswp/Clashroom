package com.clashroom.client.window;

import java.util.ArrayList;

import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

/*
 * Even though this is named a window it isn't a true window,
 * purposely does not inherit functionality of Window.java
 * This Window is meant to be used on the main page
 */

public class QuestsWindow extends Composite {
	
	public final static String NAME = "studentQuests";
	
	private static QuestRetrieverServiceAsync questRetrieverSvc = GWT
            .create(QuestRetrieverService.class);
	
	private ArrayList<QuestEntity> availableQuests;
	private FlexTable studentQuests;
	
	public QuestsWindow(){
	   if (questRetrieverSvc == null) 
	   { 
	     questRetrieverSvc = GWT.create(QuestRetrieverService.class); 
	   }
	          
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
	     listStudentQuests(/*availableQuests*/);
	   } };
	    
	   	setUpUI(); 
	   
	    questRetrieverSvc.retrieveQuests(callback);        
	}
	
	public void listStudentQuests(/*ArrayList<QuestEntity> availableQuests*/) {
        //availableQuests = this.availableQuests;
        
		String[] headers = new String[] {
				"Quest Name", "Description", "Reward", "Completion Type"
		};
		
		for (int i = 0; i < headers.length; i++) {
			studentQuests.setText(0, i, headers[i]);
		}
		
        for (int i = 0; i < availableQuests.size(); i++) 
        {	
        	studentQuests.setWidget(i + 1, 0, new QuestInnerWindow(availableQuests.get(i)));
        }
		studentQuests.setWidth("600px");
        
    }
	public String getName(){
    	return NAME;
    }
	
	public void setUpUI(){
		studentQuests = new FlexTable();
		initWidget(studentQuests);
	}
}
