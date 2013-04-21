package com.clashroom.client.window;

import java.util.Date;
import java.util.LinkedList;

import com.clashroom.client.FlowControl;
import com.clashroom.client.Styles;
import com.clashroom.client.services.TaskService;
import com.clashroom.client.services.TaskServiceAsync;
import com.clashroom.client.task.SideQuestPage;
import com.clashroom.client.widget.ProgressBar;
import com.clashroom.shared.task.ActiveTask;
import com.clashroom.shared.task.ActiveTaskList;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TasksWindow extends Window {
	
	public final static String NAME = "sideQuestWidget";
	
	private static final int REFRESH_INTERVAL = 1000; //ms
	private static final int WIDGET_LIMIT = 5;
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable queueTable = new FlexTable();
	private ProgressBar currentBar = new ProgressBar();
	private ProgressBar totalBar = new ProgressBar();
	private ActiveTaskList aql = new ActiveTaskList();
	private TaskServiceAsync questService = GWT.create(TaskService.class);
	
	public TasksWindow() {
		super();
		setup();
	}
	
	public void setup() {
		Label label = new Label("Bounties");
		label.addStyleName(Styles.text_title);
		mainPanel.add(label);
		
		//Create tables for quests
		queueTable.setText(0, 0, "Title");
		queueTable.setText(0, 1, "Time Left");
		
		// Add styles to elements in the quest tables
		queueTable.getRowFormatter().addStyleName(0, Styles.table_header);
		queueTable.getRowFormatter().addStyleName(0, Styles.gradient);
		queueTable.addStyleName(Styles.table);
		queueTable.setCellSpacing(0);
		
		//Assemble main panel
		mainPanel.add(queueTable);
		mainPanel.add(currentBar);
		mainPanel.add(totalBar);
		
		mainPanel.addStyleName(NAME);
		
		focusPanel.add(mainPanel);
		
		//Associate the main panel with the HTML host page
		initWidget(focusPanel);
		
		//Setup timer to refresh automatically
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				updateQueue();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	
		//Request data from datastore
		getActiveQuests();
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
	
	//Populates the active QuestQueue table with Quest data from the active QuestQueue
	public void updateQueue() {
		
		if (aql.isEmpty()){
			for (int i = 0; i < queueTable.getCellCount(0); i++){
				queueTable.setText(1, i, "");
			}
			queueTable.setText(1, 0, "Add some quests!");
			currentBar.setProgress(0);
			totalBar.setProgress(0);
		} else {
			DateTimeFormat dtf = DateTimeFormat.getFormat(PredefinedFormat.TIME_MEDIUM);
			DateTimeFormat dtf2 = DateTimeFormat.getFormat(PredefinedFormat.HOUR24_MINUTE_SECOND);
			
			LinkedList<ActiveTask> queue = aql.getAllQuests();
			long startTime = aql.activeQuestStart();
			
			for (int i = 0; i < queue.size() && i < WIDGET_LIMIT; i++){
				int x = i + 1;
				//Title
				queueTable.setText(x, 0, queue.get(i).getTitle());
				startTime += queue.get(i).getDuration();
				long timeLeft = startTime - new Date().getTime();
				//Time left
				queueTable.setText(x, 1, dtf2.format(new Date(timeLeft-(19*60*60*1000))));
				
				if (i == 0) queueTable.getRowFormatter().addStyleName(1, Styles.text_bold);
				
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

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void click() {
		FlowControl.go(SideQuestPage.NAME);
	}

}
