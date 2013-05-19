package com.clashroom.client.teacher;

import com.clashroom.client.Page;
import com.google.gwt.user.client.Window;

public class CreateQuestPage extends Page {
	
	public final static String NAME = "CreateQuest";
	
	private CreateQuestWidget createQuestWidget;
	
    /**
     * Default Constructor
     * */
    public CreateQuestPage() {
        this(NAME);
    }

    /**
     * Constructor that creates the page for creating a quest
     * this page should only be able to be accessed by teachers
     * 
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
