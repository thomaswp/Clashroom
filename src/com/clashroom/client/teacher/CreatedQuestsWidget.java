/**
 * AvailableQuestsWidget.java 1.0 Apr 13, 2013	
 *
 * COPYRIGHT (c) 2013 Riese P. Narcisse. All Rights Reserved 
 */
package com.clashroom.client.teacher;

import java.util.ArrayList;

import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;


/**
 * Available Quests Widget, List all Quest Available, from here
 * I want to navigate to a custom details page of every quest
 * 
 * @author Rpn
 * @version 1.0
 * 
 */
public class CreatedQuestsWidget extends Composite {
    private ArrayList<QuestEntity> availableQuests;
    private final Label questLabel = new Label("Available Quest");
    private final FlexTable questsFlexTable = new FlexTable();

    public CreatedQuestsWidget() {
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        initWidget(hPanel);
       	setStyling();
        hPanel.add(questLabel);
        hPanel.add(questsFlexTable);
    }

    /*
     * TODO: Have hyper links go to the appropriate quest info and update/delete appropriate
     * quests
     */
    public void listAvailableQuests(ArrayList<QuestEntity> availableQuests) {
        this.availableQuests = availableQuests;
        
		String[] headers = new String[] {
				"Quest Name", "Update", "Delete", "Add Quest"
		};

		for (int i = 0; i < headers.length; i++) {
			questsFlexTable.setText(0, i, headers[i]);
		}
        
        for (int i = 0; i < availableQuests.size(); i++) 
        {	
        	questsFlexTable.setWidget(i + 1, 0, 
        			new Hyperlink(availableQuests.get(i).getQuestName(),
        					QuestDetailPage.NAME+ "?id=" + availableQuests.get(i).getId()));
        	questsFlexTable.setWidget(i + 1, 1, new Hyperlink("Update","URL"));
        	questsFlexTable.setWidget(i + 1, 2, new Hyperlink("Delete","URL"));
        	questsFlexTable.setWidget(i + 1, 3, new Hyperlink("Add Quest","URL"));
        }
    }
    private void setStyling(){
    	questsFlexTable.setWidth("600px");
    	questLabel.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }
}
