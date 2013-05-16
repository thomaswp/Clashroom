package com.clashroom.client.bounty;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import com.clashroom.client.FlowControl;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.TaskService;
import com.clashroom.client.services.TaskServiceAsync;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.user.SetupPage;
import com.clashroom.client.widget.ProgressBar;
import com.clashroom.client.widget.ScrollableFlexTable;
import com.clashroom.shared.Debug;
import com.clashroom.shared.entity.UserEntity;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BountiesPage extends Page {
	
	public final static String NAME = "bounties";
	
	private static final int TASK_LIMIT =  24*60*60*1000;
	
	private static final int REFRESH_INTERVAL = 1000; //ms
	private VerticalPanel mainPanel = new VerticalPanel();
	private ScrollableFlexTable questTable = new ScrollableFlexTable();
	private ScrollableFlexTable queueTable = new ScrollableFlexTable();
	private ProgressBar currentBar = new ProgressBar();
	private ProgressBar totalBar = new ProgressBar();
	private ArrayList<Task> quests;// = new ArrayList<Quest>();
	private ActiveTaskList aql = new ActiveTaskList();
	private final static TaskServiceAsync taskService = Services.taskService;
	private final static UserInfoServiceAsync userInfoService = Services.userInfoService;
	private UserEntity user;
	private Timer refreshTimer;
	
	public BountiesPage() {
		this(NAME);
	}

	public BountiesPage(String token) {
		super(token);
		
		setup();
	}
	
	public void setup() {
		mainPanel.addStyleName(NAME);
		
		//Create tables for quests
	
		// Add styles to elements in the quest tables
		
		questTable = new ScrollableFlexTable();
		questTable.setHeaders("Title", "Description", "Reward", "Duration", "Add");
		questTable.setHeaderWidths("20%", "50%", "10%", "12.5%", "7.5%");
		questTable.setColumnWidths("20%", "55%", "10%", "10%", "5%");
		questTable.addHeaderStyles(Styles.table_header, Styles.gradient);
		questTable.getInnerTable().addStyleName(Styles.table);
		questTable.getOuterTable().addStyleName(Styles.outer_table);
		questTable.getScrollPanel().setHeight("175px");
		questTable.getInnerTable().setCellSpacing(0);
		
		queueTable = new ScrollableFlexTable();
		queueTable.setHeaders("Title", "Description", "Reward", "Time Left", "Remove");
		queueTable.setHeaderWidths("20%", "40%", "15%", "17.5%", "7.5%");
		queueTable.setColumnWidths("20%", "50%", "10%", "17.5%", "5%");
		queueTable.addHeaderStyles(Styles.table_header, Styles.gradient);
		queueTable.getInnerTable().addStyleName(Styles.table);
		queueTable.getOuterTable().addStyleName(Styles.outer_table);
		queueTable.getScrollPanel().setHeight("175px");
		queueTable.getInnerTable().setCellSpacing(0);
		
		//Assemble main panel

		Label label = new Label("Bounties");
		label.addStyleName(Styles.page_title);
		mainPanel.add(label);
		label = new Label("Available Bounties");
		label.addStyleName(Styles.page_subtitle);
		mainPanel.add(label);
		
		mainPanel.add(questTable);
		label = new Label("Queued Bounties");
		label.addStyleName(Styles.page_subtitle);
		mainPanel.add(label);

		mainPanel.add(queueTable);
		label = new Label("Current Progress");
		label.addStyleName(Styles.page_subtitle);
		mainPanel.add(label);
		mainPanel.add(currentBar);
		label = new Label("Total Progress");
		label.addStyleName(Styles.page_subtitle);
		mainPanel.add(label);
		mainPanel.add(totalBar);
		
		//Associate the main panel with the HTML host page
		//RootPanel.get("main").add(mainPanel);
		initWidget(mainPanel);
		
		//Setup timer to refresh automatically
		refreshTimer = new Timer() {
			@Override
			public void run() {
				updateQueue();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
		
		userInfoService.getUser(new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				if (!result.isSetup()) {
					FlowControl.go(new SetupPage(result));
				} else {
					user = result;
					getAvailableQuests();
					getActiveQuests(user.getId());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	
		//Request data from datastore

	}
	
	@Override
	public void cleanup() {
		refreshTimer.cancel();
	}
	
	//Clones a Quest from the QuestList and adds it to the active QuestQueue
	public void takeQuest(Task o){
		if (aql.getTotalDuration() < TASK_LIMIT){
			aql.addQuest(o);
			persistAQL();
			updateQueue();
		}
		
	}
	
	//Adds a quest to the available QuestList
	public void createQuest(Task q) {
		quests.add(q);
		updateList();
	}
	
	//Requests datastore to persist the AQl
	public void persistAQL(){
		taskService.persistAQL(user.getId(), aql, new AsyncCallback<String>() {
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
		taskService.getAvailableQuests(new AsyncCallback<ArrayList<Task>>() {
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
	public void getActiveQuests(Long userID) {
		taskService.getActiveQuests(userID, new AsyncCallback<ActiveTaskList>() {
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
			SimplePanel full = new SimplePanel();
			full.addStyleName(Styles.table_hidden_field);
			full.add(new Label(quests.get(i).getDesc()));
			questTable.setWidget(x, 1, full);
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
			questTable.getInnerTable().getColumnFormatter().addStyleName(4, Styles.text_center);
		}
	}
	
	//Populates the active QuestQueue table with Quest data from the active QuestQueue
	public void updateQueue() {
		
		if (aql.isEmpty()){
			for (int i = 0; i < queueTable.getCellCount(0); i++){
				queueTable.setText(1, i, "");
			}
			queueTable.setText(1, 1, "Add some bounties!");
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
				queueTable.setText(x, 0, queue.get(i).getTitle());
				//Description
				SimplePanel full = new SimplePanel();
				full.addStyleName(Styles.table_hidden_field);
				full.add(new Label(quests.get(i).getDesc()));
				queueTable.setWidget(x, 1, full);
				//Reward
				queueTable.setText(x, 2, Integer.toString(queue.get(i).getReward()));
				//Start time
				//queueTable.setText(x, 3, dtf.format(new Date(startTime)));
				startTime += queue.get(i).getDuration();
				//End time
				//queueTable.setText(x, 4, dtf.format(new Date(startTime)));
				long timeLeft = startTime - new Date().getTime();
				//Time left
				queueTable.setText(x, 3, dtf2.format(new Date(timeLeft-(19*60*60*1000))));
				
				if (i == 0) queueTable.getInnerTable().getRowFormatter().addStyleName(1, Styles.text_bold);
				
				queueTable.setWidget(x, 4, removeQuestButton(i));
				
				
			}
			updateProgressBar();
			
			//Check for active Quest for completion
			if (aql.activeTimeLeft() <= 0 && aql.getAllQuests().size() > 0){
				taskService.completeQuest(user.getId(), aql, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Dat shit broke");
					}

					@Override
					public void onSuccess(String result) {
						System.out.println("Page: " + result);
						aql.removeFirst();
						queueTable.removeRow(queueTable.getRowCount()-1);
						currentBar.setProgress(0);
						if (aql.getAllQuests().size() < 1){
							totalBar.setProgress(0);
						}
					}
					
				});
//				aql.completeQuest(userInfoService);
//				queueTable.removeRow(queueTable.getRowCount()-1);
//				return;
			}
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
