package com.clashroom.client.quest;

import java.util.ArrayList;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.client.services.QuestRetrieverServiceAsync;
import com.clashroom.client.services.Services;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.window.ItemInnerWindow;
import com.clashroom.shared.Constant;
import com.clashroom.shared.entity.ItemEntity;
import com.clashroom.shared.entity.QuestEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/*
 * Page that gets the details of a quest that was chosen
 * Riese Narcisse
 */
public class QuestDetailPage extends Page implements ClickHandler {

	public final static String NAME = "QuestDetails";

	private static QuestRetrieverServiceAsync questRetrieverSvc = Services.questRetrieverService;

	private static UserInfoServiceAsync userSvc = Services.userInfoService;

	private static ItemRetrieverServiceAsync itemSvc = Services.itemRetrieverService;

	private VerticalPanel vPanel;
	private HorizontalPanel hPanel;
	private Label questName;
	private Label questDesc;
	private Label questXpGained;
	private Label questLevelRequirment;
	private Label questItemsAwarded;
	private Label questDateAvailable;
	private Label questDateUnavailable;
	private FlexTable itemsListTable;
	private Button submit;
	private Button navigate;
	private TextBox enterCode;
	private Label codeEntryMsg;
	private UserEntity currentUser;
	private ArrayList<ItemEntity> questItems = new ArrayList<ItemEntity>();


	private QuestEntity aQuest = null;

	public QuestDetailPage()
	{
		this(NAME);
	}
	public QuestDetailPage(String token) {
		super(token);

		long id = getLongParameter("id");

		AsyncCallback<UserEntity> callBack = new AsyncCallback<UserEntity>(){

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Error: RPC Call Failed");
				caught.printStackTrace();		
			}

			@Override
			public void onSuccess(UserEntity result) {
				currentUser = result;
				setUpContent();
			}

		};

		// Set up the callback object.
		AsyncCallback<QuestEntity> callback = new AsyncCallback<QuestEntity>() {

			@Override 
			public void onFailure(Throwable caught) {
				System.err.println("Error: RPC Call Failed");
				caught.printStackTrace(); 
			}

			@Override 
			public void onSuccess(QuestEntity result){ 
				aQuest = result;
				setUpContent();
			} 
		};

		setupUI();
		questRetrieverSvc.retrieveAQuest(id, callback);
		getQuestItems();
		userSvc.getUser(callBack);	
	}

	public void setupUI() {

		vPanel = new VerticalPanel();
		hPanel = new HorizontalPanel();

		questName = new Label();
		questName.addStyleName(Styles.text_title);
		questDesc = new Label();
		questDesc.addStyleName(Styles.quest_desc);
		questXpGained = new Label();
		questLevelRequirment = new Label();
		questLevelRequirment.addStyleName(Styles.quest_lvl);
		questItemsAwarded = new Label();
		questDateAvailable = new Label();
		questDateAvailable.addStyleName(Styles.quest_date);
		questDateUnavailable = new Label();
		questDateUnavailable.addStyleName(Styles.quest_date);
		codeEntryMsg = new Label();
		codeEntryMsg.setVisible(false);
		codeEntryMsg.addStyleName(Styles.quest_message);
		itemsListTable = new FlexTable();
		submit = new Button("Submit Code");
		submit.setVisible(false);
		navigate = new Button("Go back home");
		navigate.setVisible(false);
		navigate.addStyleName(".gwt-return-Button");
		enterCode = new TextBox();
		enterCode.setVisible(false);

		submit.addClickHandler(this);
		navigate.addClickHandler(this);
		

		VerticalPanel rewards = new VerticalPanel();
		rewards.addStyleName(Styles.quest_rewards);
		rewards.add(questXpGained);
		rewards.add(questItemsAwarded);
		rewards.add(itemsListTable);

		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName("details");
		panel.add(questDateAvailable);
		panel.add(questDateUnavailable);
		panel.add(hPanel);

		vPanel.add(questName);
		vPanel.add(questLevelRequirment);
		vPanel.add(questDesc);

		vPanel.add(panel);

		hPanel.add(enterCode);
		hPanel.add(submit);
		vPanel.add(codeEntryMsg);

		vPanel.add(rewards);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		vPanel.addStyleName(NAME);
		
		mainPanel.add(vPanel);
		mainPanel.add(navigate);
		mainPanel.setCellHorizontalAlignment(navigate, HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(mainPanel);
	}

	private void setUpContent(){
		if (aQuest == null || questItems == null || currentUser == null) return;
		
		Window.setTitle("Quest Details: " + aQuest.getQuestName());

		questName.setText(aQuest.getQuestName());
		questDesc.setText(aQuest.getQuestDescription());
		questXpGained.setText(Constant.TERM_EXP + ": "+ aQuest.getExperienceRewarded() + Constant.TERM_EXP_SHORT);
		questLevelRequirment.setText("Level "+ aQuest.getLevelRequirement());
		questItemsAwarded.setText("Items: ");


		for(int i = 0; i < questItems.size(); i++){
			if(aQuest.getItemsRewarded().contains(questItems.get(i).getId())){
				itemsListTable.setWidget(i, 0, 
						new ItemInnerWindow(questItems.get(i)));
			}
		}
		questDateAvailable.setText("This quest will be available: " + aQuest.getDateAvailable() + " -");
		questDateUnavailable.setText(aQuest.getDateUnavailable());

		completionTypeHandler(aQuest);
	}

	private void completionTypeHandler(QuestEntity aQuest){
		if(aQuest.getCompletionCode() != null){
			submit.setVisible(true);
			enterCode.setVisible(true);
		}

	}

	private void getQuestItems(){

		AsyncCallback<ArrayList<ItemEntity>> callback = new AsyncCallback<ArrayList<ItemEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Error: RPC Call Failed");
				caught.printStackTrace();						
			}

			@Override
			public void onSuccess(ArrayList<ItemEntity> result) {
				questItems = result;
				setUpContent();
			}

		};
		itemSvc.retrieveItems(callback);

	}
	@Override
	public void onClick(ClickEvent event) {
		codeEntryMsg.setVisible(true);
		navigate.setVisible(true);
		if(event.getSource().equals(submit)){

			if(enterCode.getText().equalsIgnoreCase((aQuest.getCompletionCode()))){

				Long id = getLongParameter("id");
				if (id == null) return;

				currentUser.addCompletedQuest(id);
				for(Long itemID: aQuest.getItemsRewarded()){
					currentUser.addItemToInventory(itemID);
				}

				if(userSvc == null){
					userSvc = GWT.create(UserInfoService.class);
				}
				userSvc.completeQuest(id, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Void result) {

					}
				});
				codeEntryMsg.setText(aQuest.getVictoryText());
			}else{
				codeEntryMsg.setText("Sorry the Code you eneterd was incorrect. Please try again.");
			}
		}
		if(event.getSource().equals(navigate)){
			History.back();
		}
	}
}

