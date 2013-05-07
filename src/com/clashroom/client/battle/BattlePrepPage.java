package com.clashroom.client.battle;

import java.util.ArrayList;

import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.client.services.ItemRetrieverServiceAsync;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.shared.entity.ItemEntity;
import com.clashroom.shared.entity.ItemType;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BattlePrepPage extends Page {
	
	public final static String NAME = "PrepareForBattle";
	
	private static UserInfoServiceAsync userSvc = GWT
			.create(UserInfoService.class);
	
	private static ItemRetrieverServiceAsync itemSvc = GWT
			.create(ItemRetrieverService.class);
	
	
	private UserEntity currentUser;
	private ItemEntity selectedActiveItem = new ItemEntity();
	private ItemEntity selectedPassiveItem = new ItemEntity();
	private ArrayList<ItemEntity> activeItems = new ArrayList<ItemEntity>();
	private ArrayList<ItemEntity> passiveItems = new ArrayList<ItemEntity>();
	private ArrayList<ItemFocusPanel> allItemPanels = new ArrayList<ItemFocusPanel>();
	private FlexTable passiveItemsTable;
	private FlexTable activeItemsTable;
	private VerticalPanel vPanel;
	private Label label1;
	private Label label2;
	private ArrayList<ItemEntity> items = new ArrayList<ItemEntity>();
	
	public BattlePrepPage(){
		this(NAME);
	}

	public BattlePrepPage(String token) {
		super(token);
		
		if(userSvc == null){
			userSvc = GWT.create(UserInfoService.class);
		}
		
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
		
		setupUI();
		getQuestItems();
		userSvc.getUser(callBack);	
	}
	
	public void setupUI() {
		vPanel = new VerticalPanel();
		label1 = new Label("Select an Active Item");
		label2 = new Label("Select a Passive Item");
		activeItemsTable = new FlexTable();
		passiveItemsTable = new FlexTable();
		
		vPanel.add(label1);
		vPanel.add(activeItemsTable);
		vPanel.add(label2);
		vPanel.add(passiveItemsTable);
		initWidget(vPanel);
    }
	
	private void setUpContent(){
		Window.setTitle("Select you items for battle!");
		
		for(int i = 0; i < items.size(); i++){
			if(currentUser.getItemInventory().contains(items.get(i).getId())){
				if(items.get(i).getItemType().equals(ItemType.ACTIVE)){
					activeItems.add(items.get(i));
					
				}else if(items.get(i).getItemType().equals(ItemType.PASSIVE)){
					passiveItems.add(items.get(i));
				}
			}
		}
		
		for(int i = 0; i < activeItems.size(); i++){
			ItemFocusPanel tempPanel = new ItemFocusPanel(activeItems.get(i));
			allItemPanels.add(tempPanel);
			activeItemsTable.setWidget(i % 5, 0,tempPanel);
		}
		
		for(int i =0; i < passiveItems.size(); i++){
			ItemFocusPanel tempPanel = new ItemFocusPanel(passiveItems.get(i));
			allItemPanels.add(tempPanel);
			passiveItemsTable.setWidget(i % 5, 0,tempPanel);
		}
	}
	
	private void getQuestItems(){
		
		if(itemSvc == null){
			itemSvc = GWT.create(ItemRetrieverService.class);
		}
		AsyncCallback<ArrayList<ItemEntity>> callback = new AsyncCallback<ArrayList<ItemEntity>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Error: RPC Call Failed");
		    	caught.printStackTrace();						
			}
			
			@Override
			public void onSuccess(ArrayList<ItemEntity> result) {
				
				items = result;
				setUpContent();
			}
			
		};
			itemSvc.retrieveItems(callback);		
	}
	
	private class ItemFocusPanel extends Composite implements MouseOverHandler, ClickHandler{
		
		private ItemEntity item;
		private FocusPanel focusPanel;
		private ItemEntity blankItem = new ItemEntity();
		private FlexTable table;
		private Label itemName;
		private Image checkMark;
		private Image blankMark;
		
		public ItemFocusPanel(ItemEntity anItem){
			focusPanel = new FocusPanel();
			itemName = new Label();
			checkMark = new Image("/img/interfaceIMGS/greencheckmark.png");
			blankMark = new Image("/img/interfaceIMGS/blankmark.png");
			table = new FlexTable();
			item = anItem;
			itemName.setText(item.getName());
			focusPanel.add(itemName);
			table.setWidget(0, 0, focusPanel);
			addListeners();
			initWidget(table);
		}

		@Override
		public void onClick(ClickEvent event) {
			
			if(event != null){
				if(item.getItemType().equals(ItemType.ACTIVE)){
					selectedActiveItem = item;
					System.out.println("Active Item: "+ selectedActiveItem.getName());
					table.setWidget(0, 1,checkMark);
					
				}else if(item.getItemType().equals(ItemType.PASSIVE)){
					selectedPassiveItem = item;
					System.out.println("Passive Item: "+ selectedPassiveItem.getName());
					table.setWidget(0, 1,checkMark);
				}
				
				notifyAllItemFocusPanels(allItemPanels);
			}
		
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			focusPanel.addStyleName(Styles.w_mouseOver);
		}
		private void addListeners(){
			focusPanel.addClickHandler(this);
			focusPanel.addMouseOverHandler(this);
		}
		
		private void notifyAllItemFocusPanels(ArrayList<ItemFocusPanel> otherItemFocusPanels){
			for(ItemFocusPanel ifp: otherItemFocusPanels){
				if(!ifp.equals(this)){
					ifp.onClick(null);
				}
			}
		}
		
	}

}
