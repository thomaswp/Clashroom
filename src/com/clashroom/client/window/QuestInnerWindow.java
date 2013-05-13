package com.clashroom.client.window;

import com.clashroom.client.FlowControl;
import com.clashroom.client.teacher.QuestDetailPage;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.user.client.ui.FlexTable;

/*
 * This is a quest INNER window that will be apart of the home page
 * this window isn't meant to be used by itself rather it is meant to be put
 * inside of another widget like another flexTable
 */

public class QuestInnerWindow extends Window {
	
public final static String NAME = "questInnerWindow";
	
	private QuestEntity questEntity;
	private FlexTable questTable;//A single row flexTable to display a single quest's info
	
	public QuestInnerWindow(QuestEntity questEntity){
		super();
		this.questEntity = questEntity;
		setUpUI();
		fillTable();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void click() {
		FlowControl.go(QuestDetailPage.NAME+ "?id=" + questEntity.getId());
	}
	
	private void fillTable(){
		String codeType = null; //TODO eventually will switch to show an icon
		
		if(questEntity.getCompletionCode() != null){
			codeType = "Code";
		}
		String[] questInfo = new String[] {
				questEntity.getQuestName(),questEntity.getQuestDescription().substring(0, 35)+"...",questEntity.getExperienceRewarded()+"", codeType
		};

		for (int i = 0; i < questInfo.length; i++) {
			questTable.setText(0, i, questInfo[i]);
			if (i == 0) questTable.getColumnFormatter().setWidth(i, "25%");
			else if (i==1) questTable.getColumnFormatter().setWidth(i, "50%");
			else if (i==2) questTable.getColumnFormatter().setWidth(i, "12.5%");
			else if (i==3) questTable.getColumnFormatter().setWidth(i, "12.5%");
		}
	}
	
	private void setUpUI(){
		questTable = new FlexTable();
		focusPanel.add(questTable);
		questTable.setWidth("100%");
		initWidget(focusPanel);
	}

}
