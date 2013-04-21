package com.clashroom.client.window;

import java.util.ArrayList;

import com.clashroom.client.Styles;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
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
}
