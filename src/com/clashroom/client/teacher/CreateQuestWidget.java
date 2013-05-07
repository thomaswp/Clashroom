package com.clashroom.client.teacher;

import java.util.ArrayList;

import com.clashroom.client.FlowControl;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.Constant;
import com.clashroom.shared.entity.ItemEntity;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.datepicker.client.DatePicker;

public class CreateQuestWidget extends Composite implements ChangeHandler,FocusHandler, 
ClickHandler, SubmitHandler,SubmitCompleteHandler {
	
		private static QuestRetrieverServiceAsync questRetrieverSvc = GWT
            .create(QuestRetrieverService.class);
		
		private static ItemRetrieverServiceAsync itemsRetrieverSvc = GWT
				.create(ItemRetrieverService.class);
	
		private final Button addRemoveItem = new Button("Add Item");
	    private final ListBox autoGenerateCode = new ListBox();
	    private final Button availableAMPM = new Button("AM");
	    private final TextBox awardedXP = new TextBox();
	    private final TextBox codeInput = new TextBox();
	    private final ListBox completionType = new ListBox();
	    private ArrayList<QuestEntity> createdQuests = new ArrayList<QuestEntity>();
	    private final DatePicker dateAvailable = new DatePicker();
	    private final DatePicker dateUnavailable = new DatePicker();
	    private final HorizontalPanel hPanel = new HorizontalPanel();
	    private final HorizontalPanel hPanel2 = new HorizontalPanel();
	    private final HorizontalPanel hPanel3 = new HorizontalPanel();
	    private final HorizontalPanel hPanel4 = new HorizontalPanel();
	    private final ListBox itemsAvailable = new ListBox();
	    private ArrayList<ItemEntity> itemsAvailableList = new ArrayList<ItemEntity>();
	    private final TextBox itemsForServer = new TextBox();
	    private final ListBox itemsSelected = new ListBox();
	    private final ArrayList<ItemEntity> itemsSelectedList = new ArrayList<ItemEntity>();
	    private final Label label1 = new Label("Enter the name of the quest.");
	    private final Label label10 = new Label();
	    private final Label label11 = new Label();
	    private final Label label12 = new Label(
	                                    "When will this quest be no more?");
	    private final Label label13 = new Label();
	    private final Label label14 = new Label(
	                                    "Every adveturer needs some words of encouragement for completing a quest. What shall you tell them upon completion?");
	    private final Label label2 = new Label(
	                                    "Please tell the tale of this quest.");
	    private final Label label3 = new Label(
	                                    "How shall you know the quest is complete?");
	    private final Label label4 = new Label();
	    private final Label label5 = new Label();
	    private final Label label6 = new Label(Constant.TERM_EXP + " gained from quest");
	    private final Label label7 = new Label("Items obtained from quest:");
	    private final Label label8 = new Label(
	                                    "How shall adveturers know of this quest?");
	    private final Label label9 = new Label();
	    private final ListBox questAvailability = new ListBox();
	    private final TextArea questDescription = new TextArea();
	    private final FormPanel questForm = new FormPanel();
	    private final TextBox questName = new TextBox();
	    private final ListBox questPrereq = new ListBox();
	    private final ListBox questUnavailability = new ListBox();
	    private final TextBox requiredLvl = new TextBox();
	    private final Button submitQuest = new Button("Create Quest!");
	    private final ListBox timeAvailableHours = new ListBox();
	    private final ListBox timeAvailableMinutes = new ListBox();
	    private final ListBox timeUnAvailableHours = new ListBox();
	    private final ListBox timeUnAvailableMinutes = new ListBox();
	    private final Button unAvailableAMPM = new Button("AM");
	    private final TextArea victoryText = new TextArea();
	    private final VerticalPanel mainPanel = new VerticalPanel();
	
	public CreateQuestWidget(){	
		
	    questForm.setWidget(mainPanel);
	    initWidget(questForm);
	    retrieveDBItems();
		questForm.setAction("/clashroom/storeQuest");
        
        attachListeners();
       
        addWidgets();
        setUpWidgets();
	}

	@Override
	public void onSubmit(SubmitEvent event) {
		populateHiddenItemsField();		
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {	
		FlowControl.go(new CreatedQuestsPage());
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource().equals(addRemoveItem)) {
            if (addRemoveItem.getText().equals("Add Item")
                                            && itemsAvailable.getSelectedIndex() != -1) {
                ItemEntity removedItem = itemsAvailableList.remove(itemsAvailable
                                                .getSelectedIndex());
                
                itemsSelectedList.add(removedItem);
            }

            if (addRemoveItem.getText().equals("Remove Item")
                                            && itemsSelected.getSelectedIndex() != -1) {

                ItemEntity removedItem = itemsSelectedList.remove(itemsSelected.getSelectedIndex());
                
                itemsAvailableList.add(removedItem);
            }
            itemListsPop();
        }

        if (event.getSource().equals(availableAMPM)) {
            if (availableAMPM.getText().equals("AM")) {
                availableAMPM.setText("PM");
            } else {
                availableAMPM.setText("AM");
            }
        }
        if (event.getSource().equals(unAvailableAMPM)) {
            if (unAvailableAMPM.getText().equals("AM")) {
                unAvailableAMPM.setText("PM");
            } else {
                unAvailableAMPM.setText("AM");
            }
        }
        if (event.getSource().equals(submitQuest)) {
            questForm.submit();

        }
		
	}

	@Override
	public void onFocus(FocusEvent event) {
		
		if (event.getSource().equals(itemsAvailable)) {
            addRemoveItem.setText("Add Item");
        }

        if (event.getSource().equals(itemsSelected)) {
            addRemoveItem.setText("Remove Item");
        }		
	}

	@Override
	public void onChange(ChangeEvent event) {
		 if (completionType.isItemSelected(2)) {
	            autoGenerateCode.setVisible(true);
	            label4.setText("Auto generate scroll hidden text?");
	        } else {
	            autoGenerateCode.setVisible(false);
	            codeInput.setVisible(false);
	            label4.setText("");
	            label5.setText("");
	        }

	        if (autoGenerateCode.isItemSelected(1)) {
	            codeInput.setVisible(true);
	            label5.setText("Please enter your code.");
	        } else {
	            codeInput.setVisible(false);
	            label5.setText("");
	        }

	        if (questAvailability.isItemSelected(1)) {
	            requiredLvl.setVisible(true);
	            label9.setText("Please enter the level required to start this quest.");
	        } else {
	            requiredLvl.setVisible(false);
	            label9.setText("");
	        }

	        if (questAvailability.isItemSelected(2)) {
	            dateAvailable.setVisible(true);
	            timeAvailableHours.setVisible(true);
	            timeAvailableMinutes.setVisible(true);
	            availableAMPM.setVisible(true);
	            label10.setText("Please select the date and time the quest becomes available:");
	        } else {
	            dateAvailable.setVisible(false);
	            timeAvailableHours.setVisible(false);
	            timeAvailableMinutes.setVisible(false);
	            availableAMPM.setVisible(false);
	            label10.setText("");
	        }

	        if (questAvailability.isItemSelected(3)) {
	            label11.setText("Please select a prerequisite quest");
	            questPrereq.setVisible(true);
	            
	            if(createdQuests.size() < 1){
	            	questListPop();
	            }	            
	        } else {
	            label11.setText("");
	            questPrereq.setVisible(false);
	        }

	        if (questUnavailability.isItemSelected(1)) {
	            label13.setText("Please select the date and time this quest becomes unavailable.");
	            dateUnavailable.setVisible(true);
	            timeUnAvailableMinutes.setVisible(true);
	            timeUnAvailableHours.setVisible(true);
	            unAvailableAMPM.setVisible(true);
	        } else {
	            label13.setText("");
	            dateUnavailable.setVisible(false);
	            timeUnAvailableMinutes.setVisible(false);
	            timeUnAvailableHours.setVisible(false);
	            unAvailableAMPM.setVisible(false);
	        }
		
	}
	
	/*
     * Helper method to add times to the timeAvailable and timeExpire
     * listBoxes
     */
    private void addTimes() {
        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                timeAvailableHours.addItem("0" + i + "");
            } else {
                timeAvailableHours.addItem("" + i);
            }
        }

        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                timeAvailableMinutes.addItem("0" + i + "");
            } else {
                timeAvailableMinutes.addItem("" + i);
            }
        }

        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                timeUnAvailableHours.addItem("0" + i + "");
            } else {
                timeUnAvailableHours.addItem("" + i);
            }
        }

        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                timeUnAvailableMinutes.addItem("0" + i + "");
            } else {
                timeUnAvailableMinutes.addItem("" + i);
            }
        }

    }
    
    private void addWidgets() {
        mainPanel.add(label1);
        mainPanel.add(questName);
        mainPanel.add(label2);
        mainPanel.add(questDescription);
        mainPanel.add(label3);
        mainPanel.add(completionType);
        mainPanel.add(label4);
        mainPanel.add(autoGenerateCode);
        mainPanel.add(label5);
        mainPanel.add(codeInput);
        mainPanel.add(label6);
        mainPanel.add(awardedXP);
        mainPanel.add(label7);
        mainPanel.add(hPanel);
        hPanel.add(itemsAvailable);
        hPanel.add(itemsSelected);
        mainPanel.add(hPanel2);
        hPanel2.add(addRemoveItem);
        mainPanel.add(label8);
        mainPanel.add(questAvailability);
        mainPanel.add(label9);
        mainPanel.add(requiredLvl);
        mainPanel.add(label10);
        mainPanel.add(hPanel3);
        hPanel3.add(dateAvailable);
        hPanel3.add(timeAvailableHours);
        hPanel3.add(timeAvailableMinutes);
        hPanel3.add(availableAMPM);
        mainPanel.add(label11);
        mainPanel.add(questPrereq);
        mainPanel.add(label12);
        mainPanel.add(questUnavailability);
        mainPanel.add(hPanel4);
        hPanel4.add(dateUnavailable);
        hPanel4.add(timeUnAvailableHours);
        hPanel4.add(timeUnAvailableMinutes);
        hPanel4.add(unAvailableAMPM);
        mainPanel.add(label14);
        mainPanel.add(victoryText);
        mainPanel.add(submitQuest);
        mainPanel.add(itemsForServer);
    }
    
    private void attachListeners() {
        completionType.addChangeHandler(this);
        autoGenerateCode.addChangeHandler(this);
        addRemoveItem.addClickHandler(this);
        questAvailability.addChangeHandler(this);
        availableAMPM.addClickHandler(this);
        unAvailableAMPM.addClickHandler(this);
        questUnavailability.addChangeHandler(this);
        itemsAvailable.addFocusHandler(this);
        itemsSelected.addFocusHandler(this);
        submitQuest.addClickHandler(this);
        questForm.addSubmitHandler(this);
        questForm.addSubmitCompleteHandler(this);
    }
    
    /*
     * This method handles the population of both the items available
     * list and items selected
     */
    private void itemListsPop() {
        itemsAvailable.clear();
        itemsSelected.clear();

        for (ItemEntity item : itemsAvailableList) {
            itemsAvailable.addItem(item.getName());
        }
        for (ItemEntity item : itemsSelectedList) {
            itemsSelected.addItem(item.getName());
        }
    }
    
    /*
     * A way to pass the items list to the servlet, best way I can
     * think of for now, should be rewritten.
     */
    private void populateHiddenItemsField() {
        itemsForServer.setText("");
        String itemIdsConcat = "";
        for (ItemEntity item : itemsSelectedList) {
            itemIdsConcat += item.getId() + ",";
        }
        itemsForServer.setText(itemIdsConcat);
    }
    
    private void retrieveDBItems() {
    	
    	if (itemsRetrieverSvc == null) 
        { 
        	itemsRetrieverSvc = GWT.create(ItemRetrieverService.class); 
        }
          
          	// Set up the callback object.
        	AsyncCallback<ArrayList<ItemEntity>> callback = new
        	AsyncCallback<ArrayList<ItemEntity>>() {
          
          @Override public void onFailure(Throwable caught) {
          System.err.println("Error: RPC Call Failed");
          caught.printStackTrace(); 
         }
          
          @Override public void onSuccess(ArrayList<ItemEntity>result) 
          { 
        	  itemsAvailableList = result;
        	  itemListsPop();
        	  
          } };
          
          itemsRetrieverSvc.retrieveItems(callback);
          
    }
    
    private void questListPop(){
    	
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
        	  createdQuests = result;
        	  
        	  for (QuestEntity quest : createdQuests) {
                  questPrereq.addItem(quest.getQuestName());
              }
        	  
          } };
          
          questRetrieverSvc.retrieveQuests(callback);
    	
    }

    private void setUpWidgets() {
        questName.setName("Quest Name");
        questDescription.setName("Quest Description");
        questDescription.setCharacterWidth(80);
        questDescription.setVisibleLines(10);
        victoryText.setCharacterWidth(80);
        victoryText.setVisibleLines(10);
        completionType.setName("Completion Type");
        completionType.addItem("Based On adveturer's honor", "Honor");
        completionType.addItem("Completion given by Overseer", "Teacher");
        completionType.addItem("Completion obtained through secret scroll text",
                                        "Code");
        codeInput.setName("Code Input");
        codeInput.setVisible(false);
        autoGenerateCode.setName("Auto Generate Code");
        autoGenerateCode.addItem("Yes");
        autoGenerateCode.addItem("No");
        autoGenerateCode.setVisible(false);
        awardedXP.setName("Awarded XP");
        itemsAvailable.setVisibleItemCount(10);
        itemsSelected.setVisibleItemCount(10);
        itemsAvailable.setPixelSize(200, 300);
        itemsSelected.setPixelSize(200, 300);
        itemsForServer.setName("Items Selected");
        itemsForServer.setVisible(false);
        questAvailability.setName("Quest Availability");
        questAvailability.addItem("I will tell the adveturers about the quest.",
                                        "Teacher");
        questAvailability.addItem("The quest will be revealed to those experienced enough",
                                        "Experience");
        questAvailability.addItem("This quest shall be revealed in due time",
                                        "DateTime");
        questAvailability.addItem("The adveturer must complete a certain quest prior to going on this one.",
                                        "Prior Quest");

        questAvailability.addItem("Word has spread quick, and the quest is already known by all.");
        requiredLvl.setName("Level Requirement");
        requiredLvl.setVisible(false);
        dateAvailable.setVisible(false);
        dateUnavailable.setVisible(false);
        addTimes();
        timeAvailableHours.setVisible(false);
        timeAvailableMinutes.setVisible(false);
        timeAvailableHours.setVisibleItemCount(1);
        timeAvailableMinutes.setVisibleItemCount(1);
        availableAMPM.setVisible(false);
        questPrereq.setVisible(false);
        questPrereq.setName("Prereq Quest");
        questUnavailability.setName("Quest Unavailablility");
        questUnavailability.addItem("This quest is always available.");
        questUnavailability.addItem("Time is a factor. There is much at stake.");
        timeUnAvailableHours.setVisible(false);
        timeUnAvailableMinutes.setVisible(false);
        timeUnAvailableHours.setVisibleItemCount(1);
        timeUnAvailableMinutes.setVisibleItemCount(1);
        unAvailableAMPM.setVisible(false);
        victoryText.setName("Victory Text");

    }
}
