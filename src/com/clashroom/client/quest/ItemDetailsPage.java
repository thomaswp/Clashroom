package com.clashroom.client.quest;

import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.client.services.Services;
import com.clashroom.shared.entity.ItemEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ItemDetailsPage extends Page {
	
	public final static String NAME = "ItemDetails";
	
	private static ItemRetrieverServiceAsync itemSvc = Services.itemRetrieverService;
	
	private VerticalPanel vPanel;
	private HorizontalPanel hPanel;
	private Label itemName;
	private Label itemDesc;
	private Label itemType;
	private String imgDir = "";
	
	private ItemEntity item = null;
	
	/*
	*  Default Constructor
	*/
	public ItemDetailsPage()
	{
		this(NAME);
	}
	
	/*
	* Constructor that grabs the appropriate item based on the id parameter attatched
	* to the url and creates a page that displays the details of that item.
	*
	* @param String token
	*/
	public ItemDetailsPage(String token) {
		super(token);
		
		long id = getLongParameter("id");
		
		AsyncCallback<ItemEntity> callback = 
				new AsyncCallback<ItemEntity>() {

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
			setupUI();
		
	}
	/*
	* Helper method to seperate out the code needed to set up the user interface
	*/
	public void setupUI() {
		
		vPanel = new VerticalPanel();
		hPanel = new HorizontalPanel();
		
		itemName = new Label();
		itemName.addStyleName(Styles.page_title);
		itemDesc = new Label();
		itemDesc.addStyleName(Styles.quest_desc);
		itemType = new Label();
		itemType.addStyleName(Styles.quest_lvl);
		
		VerticalPanel img = new VerticalPanel();
		img.add(new Image(imgDir));
		
		vPanel.add(itemName);
		vPanel.add(itemDesc);
		vPanel.add(img);
		vPanel.add(hPanel);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		vPanel.addStyleName(NAME);
		
		mainPanel.add(vPanel);
        	initWidget(mainPanel);
    }

	private void setUpContent(){
		Window.setTitle("Item Details: " + item.getName());
		
		itemName.setText(item.getName());
		itemDesc.setText(item.getDescription());
		
		imgDir = item.getImageDir();
	}

}
