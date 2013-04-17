package com.clashroom.client.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import com.clashroom.client.HomePage;
import com.clashroom.client.Styles;
import com.clashroom.client.page.Page;
import com.clashroom.client.services.TaskService;
import com.clashroom.client.services.TaskServiceAsync;
import com.clashroom.client.widget.ProgressBar;
import com.clashroom.shared.task.ActiveTask;
import com.clashroom.shared.task.ActiveTaskList;
import com.clashroom.shared.task.Task;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SideQuestPage extends Page {
	
	public final static String NAME = "sideQuests";
	
	private static final int REFRESH_INTERVAL = 1000; //ms
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable questTable = new FlexTable();
	private FlexTable queueTable = new FlexTable();
	private ProgressBar currentBar = new ProgressBar();
	private ProgressBar totalBar = new ProgressBar();
	private ArrayList<Task> quests;// = new ArrayList<Quest>();
	private ActiveTaskList aql = new ActiveTaskList();
	private TaskServiceAsync questService = GWT.create(TaskService.class);
	
	public SideQuestPage() {
		this(NAME);
	}

	public SideQuestPage(String token) {
		super(token);
		
		setup();
	}
	
	public void setup() {
		mainPanel.addStyleName(NAME);
		
		//Create tables for quests
		questTable.setText(0, 0, "Title");
		questTable.setText(0, 1, "Description");
		questTable.setText(0, 2, "Reward");
		questTable.setText(0, 3, "Duration");
		questTable.setText(0, 4, "Add");
		
		queueTable.setText(0, 0, "Title");
		queueTable.setText(0, 1, "Description");
		queueTable.setText(0, 2, "Reward");
		queueTable.setText(0, 3, "Start");
		queueTable.setText(0, 4, "End");
		queueTable.setText(0, 5, "Time Left");
		queueTable.setText(0, 6, "Remove");
		
		// Add styles to elements in the quest tables
		questTable.getRowFormatter().addStyleName(0, Styles.squest_header);
		questTable.getRowFormatter().addStyleName(0, Styles.gradient);
		questTable.addStyleName(Styles.squest_table);
		questTable.getColumnFormatter().addStyleName(4, Styles.text_center);
		
		queueTable.getRowFormatter().addStyleName(0, Styles.squest_header);
		queueTable.getRowFormatter().addStyleName(0, Styles.gradient);
		queueTable.addStyleName(Styles.squest_table);
		
		//Assemble main panel
		Hyperlink link = new Hyperlink("Back", HomePage.NAME);
		mainPanel.add(link);
		mainPanel.add(questTable);
		mainPanel.add(queueTable);
		mainPanel.add(currentBar);
		mainPanel.add(totalBar);
		
		//Associate the main panel with the HTML host page
		//RootPanel.get("main").add(mainPanel);
		initWidget(mainPanel);
		
		//Setup timer to refresh automatically
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				updateQueue();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	
		//Request data from datastore
		getAvailableQuests();
		getActiveQuests();
	}
	
	//Clones a Quest from the QuestList and adds it to the active QuestQueue
	public void takeQuest(Task o){
		aql.addQuest(o);
		persistAQL();
		updateQueue();
	}
	
	//Adds a quest to the available QuestList
	public void createQuest(Task q) {
		quests.add(q);
		updateList();
	}
	
	//Requests datastore to persist the AQl
	public void persistAQL(){
		questService.persistAQL(aql, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				System.out.println(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failed to persist aql");
				caught.printStackTrace();
			}
		});
	}
	
	//Requests datastore for the list of available quests
	public void getAvailableQuests(){
		questService.getAvailableQuests(new AsyncCallback<ArrayList<Task>>() {
			@Override
			public void onSuccess(ArrayList<Task> result) {
				quests = result;
				updateList();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	//Requests datastore for the list of Active quests
	public void getActiveQuests() {
		questService.getActiveQuests(new AsyncCallback<ActiveTaskList>() {
			@Override
			public void onSuccess(ActiveTaskList result) {
				aql = result;
				updateQueue();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	//Populates QuestList table from the Quest data in the available QuestList
	public void updateList() {
		for (int i = 0; i < quests.size(); i++){
			final int x = i + 1;
			//Title
			questTable.setText(x, 0, quests.get(i).getTitle());
			//Description
			questTable.setText(x, 1, quests.get(i).getDesc().substring(0, 30)+"...");
			//Reward
			questTable.setText(x, 2, Integer.toString(quests.get(i).getReward()));
			DateTimeFormat dtf2 = DateTimeFormat.getFormat(PredefinedFormat.HOUR24_MINUTE_SECOND);
			//Duration
			questTable.setText(x, 3, dtf2.format(new Date(quests.get(i).getDuration()-(19*60*60*1000))));
			
			Button addQuestButton = new Button("+");
			addQuestButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					takeQuest(quests.get(x-1));
				}
			});
			addQuestButton.addStyleName(Styles.gradient);
			questTable.setWidget(x, 4, addQuestButton);
			questTable.getColumnFormatter().addStyleName(4, Styles.text_center);
		}
	}
	
	//Populates the active QuestQueue table with Quest data from the active QuestQueue
	public void updateQueue() {
		
		if (aql.isEmpty()){
			for (int i = 0; i < queueTable.getCellCount(0); i++){
				queueTable.setText(1, i, "");
			}
			queueTable.setText(1, 1, "Add some quests!");
			currentBar.setProgress(0);
			totalBar.setProgress(0);
		} else {
			DateTimeFormat dtf = DateTimeFormat.getFormat(PredefinedFormat.TIME_MEDIUM);
			DateTimeFormat dtf2 = DateTimeFormat.getFormat(PredefinedFormat.HOUR24_MINUTE_SECOND);
			
			LinkedList<ActiveTask> queue = aql.getAllQuests();
			long startTime = aql.activeQuestStart();
			
			for (int i = 0; i < queue.size(); i++){
				int x = i + 1;
				//Title
				queueTable.setText(x, 0, queue.get(i).getTitle() + " " + i);
				//Description
				queueTable.setText(x, 1, queue.get(i).getDesc().substring(0, 25)+"...");
				//Reward
				queueTable.setText(x, 2, Integer.toString(queue.get(i).getReward()));
				//Start time
				queueTable.setText(x, 3, dtf.format(new Date(startTime)));
				startTime += queue.get(i).getDuration();
				//End time
				queueTable.setText(x, 4, dtf.format(new Date(startTime)));
				long timeLeft = startTime - new Date().getTime();
				//Time left
				queueTable.setText(x, 5, dtf2.format(new Date(timeLeft-(19*60*60*1000))));
				
				if (i == 0) queueTable.getRowFormatter().addStyleName(1, Styles.text_bold);
				
				queueTable.setWidget(x, 6, removeQuestButton(i));
				
				//Check for active Quest for completion
				if (aql.activeTimeLeft() <= 0){
					aql.completeQuest();
					queueTable.removeRow(queueTable.getRowCount()-1);
					return;
				}
			}
			updateProgressBar();
		}
	}
	
	//Updates the ProgressBar widget based on Quest data
	public void updateProgressBar() {
		currentBar.setMaxProgress(aql.activeDuration());
		currentBar.setProgress(aql.activeDuration() - aql.activeTimeLeft());
		totalBar.setMaxProgress(aql.getTotalDuration());
		totalBar.setProgress(aql.getTotalDuration() - aql.totalTimeLeft());
	}
	
	//Helper method to create the remove quest button
	public Button removeQuestButton(final int i){
		Button removeQuestButton = new Button("-");
		removeQuestButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				aql.removeQuest(i);
				persistAQL();
				queueTable.removeRow(i+1);
				//updateQueue();
			}
		});
		removeQuestButton.addStyleName(Styles.gradient);
		return removeQuestButton;
	}

}
