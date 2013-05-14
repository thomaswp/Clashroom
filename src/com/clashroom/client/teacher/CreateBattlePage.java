package com.clashroom.client.teacher;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.clashroom.client.FlowControl;
import com.clashroom.client.Page;
import com.clashroom.client.services.BattleService;
import com.clashroom.client.services.BattleServiceAsync;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.widget.VerticalPanelWithSpacer;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.Battle;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tractionsoftware.gwt.user.client.ui.UTCDateBox;
import com.tractionsoftware.gwt.user.client.ui.UTCTimeBox;

public class CreateBattlePage extends Page {

	public final static String NAME = "CreateBattle";

	private UserInfoServiceAsync userInfoService = Services.userInfoService;
	private BattleServiceAsync battleService = Services.battleService;

	private PickupDragController dragController;

	private HashMap<Label, UserEntity> labelMap = new HashMap<Label, UserEntity>();
	
	private VerticalPanel vPanelUsers, vPanelTeamA, vPanelTeamB;
	private Button buttonCreate;
	private UTCTimeBox timeBox;
	private UTCDateBox dateBox;

	public CreateBattlePage(String token) {
		super(token);
		setupUI();
		userInfoService.getAllUsers(new AsyncCallback<List<UserEntity>>() {
			@Override
			public void onSuccess(List<UserEntity> result) {
				populateUI(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});

	}

	public void setupUI() {
		VerticalPanel vPanelHost = new VerticalPanel();
		
		AbsolutePanel boundaryPanel = new AbsolutePanel();
		boundaryPanel.setPixelSize(600, 400);
		vPanelHost.add(boundaryPanel);

		dragController = new PickupDragController(boundaryPanel, false);
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		boundaryPanel.add(hPanel);

		VerticalPanel parent;
		
		vPanelUsers = new VerticalPanelWithSpacer();
		vPanelUsers.setWidth("200px");
		vPanelUsers.setHeight("100%");
		dragController.registerDropController(new VerticalPanelDropController(vPanelUsers));
		parent = new VerticalPanel();
		parent.add(new Label("Users"));
		parent.add(vPanelUsers);
		hPanel.add(parent);
		
		vPanelTeamA = new VerticalPanelWithSpacer();
		vPanelTeamA.setWidth("200px");
		vPanelTeamA.setHeight("100%");
		dragController.registerDropController(new VerticalPanelDropController(vPanelTeamA));
		parent = new VerticalPanel();
		parent.add(new Label("Team A"));
		parent.add(vPanelTeamA);
		hPanel.add(parent);
		
		vPanelTeamB = new VerticalPanelWithSpacer();
		vPanelTeamB.setWidth("200px");
		vPanelTeamB.setHeight("100%");
		dragController.registerDropController(new VerticalPanelDropController(vPanelTeamB));
		parent = new VerticalPanel();
		parent.add(new Label("Team B"));
		parent.add(vPanelTeamB);
		hPanel.add(parent);
		
		HorizontalPanel datePanel = new HorizontalPanel();
		dateBox = new UTCDateBox();
		dateBox.setValue(System.currentTimeMillis());
		datePanel.add(new Label("Date: "));
		datePanel.add(dateBox);
		vPanelHost.add(datePanel);
		
		
		HorizontalPanel timePanel = new HorizontalPanel();
		timeBox = new UTCTimeBox();
		timeBox.setValue(System.currentTimeMillis() - dateBox.getValue());
		timePanel.add(new Label("Time: "));
		timePanel.add(timeBox);
		vPanelHost.add(timePanel);
		
		buttonCreate = new Button("Create Battle");
		buttonCreate.setEnabled(false);
		vPanelHost.add(buttonCreate);
		
		
		initWidget(vPanelHost);
	}

	public void populateUI(List<UserEntity> users) {
		
		for (UserEntity user : users) {
			Label label = new Label(Formatter.format("%s (%s)", 
					user.getUsername(), user.getDragon().getName()), false);
			labelMap.put(label, user);
			vPanelUsers.add(label);
			dragController.makeDraggable(label);
		}
		
		buttonCreate.setEnabled(true);
		buttonCreate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<Long> teamAIds = new LinkedList<Long>();
				List<Long> teamBIds = new LinkedList<Long>();
				
				List<UserEntity> teamA = new LinkedList<UserEntity>();
				for (int i = 0; i < vPanelTeamA.getWidgetCount(); i++) {
					Widget widget = vPanelTeamA.getWidget(i);
					if (labelMap.containsKey(widget)) {
						UserEntity entity = labelMap.get(widget);
						teamAIds.add(entity.getId());
						teamA.add(entity);
					}
				}
				List<UserEntity> teamB = new LinkedList<UserEntity>();
				for (int i = 0; i < vPanelTeamB.getWidgetCount(); i++) {
					Widget widget = vPanelTeamB.getWidget(i);
					if (labelMap.containsKey(widget)) {
						UserEntity entity = labelMap.get(widget);
						teamBIds.add(entity.getId());
						teamB.add(entity);
					}
				}
				if (teamAIds.isEmpty() || teamBIds.isEmpty()) return;
				
				buttonCreate.setEnabled(false);
				
				String teamAName = Battle.getTeamName(teamA);
				String teamBName = Battle.getTeamName(teamB);
				Date date = new Date(dateBox.getValue() + timeBox.getValue());
				battleService.scheduleBattle(teamAName, teamAIds, teamBName, teamBIds, date, 
						new AsyncCallback<Long>() {
					@Override
					public void onSuccess(Long result) {
						if (result == null) {
							buttonCreate.setEnabled(true);
							Debug.write("Failure!");
						} else {
							FlowControl.go(new CreateBattlePage(NAME));
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						buttonCreate.setEnabled(true);
						caught.printStackTrace();
					}
				});
			}
		});
	}

}
