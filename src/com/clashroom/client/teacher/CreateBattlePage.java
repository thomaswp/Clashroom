package com.clashroom.client.teacher;

import java.util.List;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.clashroom.client.Page;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.widget.VerticalPanelWithSpacer;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CreateBattlePage extends Page {

	public final static String NAME = "CreateBattle";

	private UserInfoServiceAsync userInfoService = GWT.create(UserInfoService.class);

	private PickupDragController dragController;

	private VerticalPanel vPanelUsers, vPanelTeamA, vPanelTeamB;
	private Button buttonCreate;

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
		
		buttonCreate = new Button("Create Battle");
		buttonCreate.setEnabled(false);
		vPanelHost.add(buttonCreate);
		
		
		initWidget(vPanelHost);
	}

	public void populateUI(List<UserEntity> users) {
		//		int row = 1;
		//		for (UserEntity user : users) {
		//			String name = Formatter.format("%s/%s (%s %s)", user.getUsername(), user.getDragon().getName(), user.getFirstName(), user.getLastName());
		//			table.setText(row, 0, name);
		//			row++;
		//		}
		
		for (UserEntity user : users) {
			Label label = new Label(user.getUsername(), false);
			vPanelUsers.add(label);
			dragController.makeDraggable(label);
		}
		
		buttonCreate.setEnabled(true);
		buttonCreate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
			}
		});
	}

}
