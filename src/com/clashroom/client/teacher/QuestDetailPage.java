package com.clashroom.client.teacher;

import com.clashroom.client.page.Page;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/*
 * Page that gets the details of a quest that was chosen
 * Riese Narcisse
 */
public class QuestDetailPage extends Page {

	public final static String NAME = "QuestDetails";
	
	private static QuestRetrieverServiceAsync questRetrieverSvc = GWT
            .create(QuestRetrieverService.class);
	
	private Label questLabel;
	private HorizontalPanel hPanel;
	
	private QuestEntity aQuest = null;
	
	public QuestDetailPage()
	{
		this(NAME);
	}
	public QuestDetailPage(String token) {
		super(token);
		
		long id = getLongParameter("id");
	          
	     // Set up the callback object.
	     AsyncCallback<QuestEntity> callback = new
	     AsyncCallback<QuestEntity>() {
	          
	     @Override 
	     public void onFailure(Throwable caught) {
	    	 System.err.println("Error: RPC Call Failed");
	    	 caught.printStackTrace(); 
	     }
	          
	          @Override 
	     public void onSuccess(QuestEntity result){ 
	        	  aQuest = result;
		          populate(aQuest);
	     } };
	     setupUI();
	          
	     questRetrieverSvc.retrieveAQuest(id, callback);
	          
	          
	}
	
	public void setupUI() {
		hPanel = new HorizontalPanel();
		hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		questLabel = new Label();
        hPanel.add(questLabel);
        initWidget(hPanel);

    }
	
	private void populate(QuestEntity aQuest) {
        Window.setTitle("Quest Details: " + aQuest.getQuestName());
        questLabel.setText(aQuest.getQuestName());
	}

}

