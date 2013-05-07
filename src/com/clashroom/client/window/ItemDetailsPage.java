package com.clashroom.client.window;

import java.util.ArrayList;

import com.clashroom.client.Page;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.shared.entity.ItemEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ItemDetailsPage extends Page {
	
	public final static String NAME = "ItemDetails";
	
	private static ItemRetrieverServiceAsync itemSvc = GWT
			.create(ItemRetrieverService.class);
	
	private VerticalPanel vPanel;
	private HorizontalPanel hPanel;
	private Label itemName;
	private Label itemtDesc;
	private Label itemType;
	
	private ItemEntity item = new ItemEntity();
	
	public ItemDetailsPage(){
		this(NAME);
	}

	public ItemDetailsPage(String token) {
		super(token);
		
		long id = getLongParameter("id");
		
		AsyncCallback<ItemEntity> callback = new AsyncCallback<ItemEntity>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Error: RPC Call Failed");
		    	caught.printStackTrace();						
			}
			
			@Override
			public void onSuccess(ItemEntity result) {
				
				item = result;
				setUpContent();
			}
			
		};
			itemSvc.retrieveAnItem(id,callback);
	}
	
	private void setUpContent(){
		
		Window.setTitle("Item Details: " + item.getName());
		
//		questName.setText(aQuest.getQuestName());
//		questDesc.setText(aQuest.getQuestDescription());
//		questXpGained.setText("Expereince: "+ aQuest.getExperienceRewarded() + " xp");
//		questLevelRequirment.setText("Level "+ aQuest.getLevelRequirement());
//		questItemsAwarded.setText("Items: ");	
	}

}