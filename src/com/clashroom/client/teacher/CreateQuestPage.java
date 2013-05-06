package com.clashroom.client.teacher;

import java.util.ArrayList;

import com.clashroom.client.Page;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateQuestPage extends Page {
	
	public final static String NAME = "CreateQuest";
	
	private CreateQuestWidget createQuestWidget;
	
	private ArrayList<QuestEntity> createdQuests;

    public CreateQuestPage() {
        this(NAME);
    }

    /**
     * @param aToken
     */
    public CreateQuestPage(String aToken) {
        super(aToken);
        setupUI();
    }

    private void setupUI() {
        Window.setTitle("Create a Quest!");
        createQuestWidget = new CreateQuestWidget();
        initWidget(createQuestWidget);
    }
    
}
