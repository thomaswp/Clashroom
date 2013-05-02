package com.clashroom.client.resources;

import java.util.ArrayList;

import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * Utility class that is meant to factor out some of the code
 * involved with creating an using services and packaging it all up here
 * Example user info, quests, items, etc.
 */
public class ServicesUtils {
	
	private static ArrayList<QuestEntity> resultContainer;
	
	private static QuestRetrieverServiceAsync questRetrieverSvc = GWT
            .create(QuestRetrieverService.class);
	
	private ServicesUtils(){
		
	}
	
	public static ArrayList<QuestEntity> retrieveAllQuests(){
		
		resultContainer = new ArrayList<QuestEntity>();
		
		if (questRetrieverSvc == null) 
		{ 
		  questRetrieverSvc = GWT.create(QuestRetrieverService.class); 
		}
		          
		   // Set up the callback object.
		AsyncCallback<ArrayList<QuestEntity>> callback = new AsyncCallback<ArrayList<QuestEntity>>() {
		          
		@Override public void onFailure(Throwable caught) {
		 System.err.println("Error: RPC Call Failed");
		 caught.printStackTrace(); 
		}
		          
		@Override public void onSuccess(ArrayList<QuestEntity>result) 
		{ 
			resultContainer = result;
		} };
		    
		    
		questRetrieverSvc.retrieveQuests(callback);
		
		return resultContainer;	
	}

}
