package com.clashroom.client.teacher;

import com.clashroom.client.page.Page;
import com.google.gwt.user.client.Window;

public class CreateQuestPage extends Page {
	
	public final static String NAME = "CreateQuest";
	
	private CreateQuestWidget createQuestWidget;

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

    public void setupUI() {
        Window.setTitle("Create a Quest!");
        createQuestWidget = new CreateQuestWidget();
        initWidget(createQuestWidget);
    }
}
