package com.clashroom.client.teacher;

import com.clashroom.client.Page;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 * Page that gets the details of a quest that was chosen
 * Riese Narcisse
 */
public class QuestDetailPage extends Page implements ClickHandler {

	public final static String NAME = "QuestDetails";
	
	private static QuestRetrieverServiceAsync questRetrieverSvc = GWT
            .create(QuestRetrieverService.class);
	
	private VerticalPanel vPanel;
	private HorizontalPanel hPanel;
	private Label questName;
	private Label questDesc;
	private Label questXpGained;
	private Label questLevelRequirment;
	private Label questItemsAwarded;
	private Label questDateAvailable;
	private Label questDateUnavailable;
	private FlexTable itemsListTable;
	private Button submit;
	private TextBox enterCode;
	private Label codeEntryMsg;
	
	
	private QuestEntity aQuest = null;
	
	public QuestDetailPage()
	{
		this(NAME);
	}
	public QuestDetailPage(String token) {
		super(token);
		
		long id = getLongParameter("id");
		
		 if (questRetrieverSvc == null) 
	        { 
	        	questRetrieverSvc = GWT.create(QuestRetrieverService.class); 
	        }
	          
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
	        	  setUpContent(aQuest);
	     } };
	     
   	  		setupUI();
	          questRetrieverSvc.retrieveAQuest(id, callback);
	          
	}
	
	public void setupUI() {
		
		vPanel = new VerticalPanel();
		hPanel = new HorizontalPanel();
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		questName = new Label();
		questDesc = new Label();
		questXpGained = new Label();
		questLevelRequirment = new Label();
		questItemsAwarded = new Label();
		questDateAvailable = new Label();
		questDateUnavailable = new Label();
		codeEntryMsg = new Label();
		codeEntryMsg.setVisible(false);
		itemsListTable = new FlexTable();
		submit = new Button("Submit Code");
		submit.setVisible(false);
		enterCode = new TextBox();
		enterCode.setVisible(false);
		
		submit.addClickHandler(this);
		
		vPanel.add(questName);
		vPanel.add(questDesc);
		vPanel.add(questXpGained);
		vPanel.add(questLevelRequirment);
		vPanel.add(questItemsAwarded);
		vPanel.add(itemsListTable);
		vPanel.add(questDateAvailable);
		vPanel.add(questDateUnavailable);
		hPanel.add(enterCode);
		hPanel.add(submit);
		hPanel.add(codeEntryMsg);
		vPanel.add(hPanel);
		
        initWidget(vPanel);
    }
	
	private void setUpContent(QuestEntity aQuest){
		Window.setTitle("Quest Details: " + aQuest.getQuestName());
		
		questName.setText("Quest Name: " + aQuest.getQuestName());
		questDesc.setText("Quest Description: " +aQuest.getQuestDescription());
		questXpGained.setText("Expereince Gained: "+ aQuest.getExperienceRewarded());
		questLevelRequirment.setText("Required Level for Quest: "+ aQuest.getLevelRequirement());
		questItemsAwarded.setText("Items Earned: ");
		
		for(int i = 0; i < aQuest.getItemsRewarded().size(); i++){
			itemsListTable.setWidget(i, 0, 
					new Hyperlink(aQuest.getItemsRewarded().get(i),"url"));
			//TODO Have these has hyperlinks go to Pages of items
		}
		questDateAvailable.setText("This quest will be available: " + aQuest.getDateAvailable());
		questDateUnavailable.setText("This quest will be no more on: " + aQuest.getDateUnavailable());
		
		completionTypeHandler(aQuest);
	}
	
	private void completionTypeHandler(QuestEntity aQuest){
		if(aQuest.getCompletionCode() != null){
			submit.setVisible(true);
			enterCode.setVisible(true);
		}
		
	}
	@Override
	public void onClick(ClickEvent event) {
		codeEntryMsg.setVisible(true);
		if(event.getSource().equals(submit)){
			if(enterCode.getText().equals(aQuest.getCompletionCode())){
				//aQuest.completeQuest();
				//TODO: Have Quest update on the server as completed	
				codeEntryMsg.setText("Congratulations!: " + aQuest.getVictoryText());
			}else{
				codeEntryMsg.setText("Sorry the Code you eneterd was incorrect. Please try again.");
			}
		}
	}
	private static class MyDialog extends DialogBox {
		private VerticalPanel vPanel;
		private Label msg;
		Button ok;
		
	    public MyDialog(String title,String messege) {
	      // Set the dialog box's caption.
	      setText(title);
	      msg = new Label(messege);
	      vPanel = new VerticalPanel();
	      ok = new Button("OK");
	      ok.setPixelSize(100, 100);
	      // DialogBox is a SimplePanel, so you have to set its widget property to
	      // whatever you want its contents to be.
	      
	      vPanel.add(msg);
	      vPanel.add(ok);
	      
	      ok.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          MyDialog.this.hide();
	        }
	      });
	      setWidget(vPanel);
	    }
	  }

}

