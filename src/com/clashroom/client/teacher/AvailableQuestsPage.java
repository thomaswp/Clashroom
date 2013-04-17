/**
 * AvailableQuestPage.java 1.0 Apr 13, 2013	
 *
 * COPYRIGHT (c) 2013 Riese P. Narcisse. All Rights Reserved 
 */
package com.clashroom.client.teacher;

import java.util.ArrayList;

import com.clashroom.client.page.Page;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Start with summary description line.
 * 
 * @author Rpn
 * @version 1.0
 * 
 */
public class AvailableQuestsPage extends Page {

    public final static String NAME = "AvailableQuests";

    private static QuestRetrieverServiceAsync questRetrieverSvc = GWT
                                    .create(QuestRetrieverService.class);

    private ArrayList<QuestEntity> availableQuests;
    private AvailableQuestsWidget questsWidget;

    public AvailableQuestsPage() {
        this(NAME);
    }

    /**
     * @param aToken
     */
    public AvailableQuestsPage(String aToken) {
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

    public void setupUI() {
        Window.setTitle("Available Quests");
        questsWidget = new AvailableQuestsWidget();
        initWidget(questsWidget);

    }

    private void populateTable() {
        questsWidget.listAvailableQuests(availableQuests);
    }
}
