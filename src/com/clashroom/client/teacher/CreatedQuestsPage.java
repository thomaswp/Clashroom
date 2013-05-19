/**
 * CreatedQuestsPage.java 1.0 Apr 13, 2013	
 *
 * COPYRIGHT (c) 2013 Riese P. Narcisse. All Rights Reserved 
 */
package com.clashroom.client.teacher;

import java.util.ArrayList;

import com.clashroom.client.Page;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.client.services.Services;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Page that displays all the quest created that are in the database.
 * This page is for teachers. This is where a teacher can edit, update and delete
 * quests.
 * 
 * @author Rpn
 * @version 1.0
 * 
 */
public class CreatedQuestsPage extends Page {

    public final static String NAME = "AvailableQuests";

    private static QuestRetrieverServiceAsync questRetrieverSvc = Services.questRetrieverService;

    private ArrayList<QuestEntity> availableQuests;
    private CreatedQuestsWidget questsWidget;

    public CreatedQuestsPage() {
        this(NAME);
    }

    /**
     * Constructor
     * 
     * @param aToken
     */
    public CreatedQuestsPage(String aToken) {
        super(aToken);
        setupUI();
        
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
        	  populateTable(); 
          } };
          
          questRetrieverSvc.retrieveQuests(callback);
         
    }
    
    /**
     * Helper method that creates a new CreatedQuestsWidget for display
     * 
     * */
    public void setupUI() {
        Window.setTitle("Available Quests");
        questsWidget = new CreatedQuestsWidget();
        initWidget(questsWidget);

    }

    private void populateTable() {
        questsWidget.listAvailableQuests(availableQuests);
    }
}
