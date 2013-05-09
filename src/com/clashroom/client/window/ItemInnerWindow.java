package com.clashroom.client.window;

import com.clashroom.client.FlowControl;
import com.clashroom.client.teacher.ItemDetailsPage;
import com.clashroom.shared.entity.ItemEntity;
import com.google.gwt.user.client.ui.Label;

public class ItemInnerWindow extends Window {
	public final static String NAME = "itemInnerWindow";
	private ItemEntity item;
	private Label itemName;
	
	public ItemInnerWindow(ItemEntity item){
		super();
		this.item = item;
		itemName = new Label(item.getName());
		focusPanel.add(itemName);
		initWidget(focusPanel);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void click() {
		FlowControl.go(ItemDetailsPage.NAME+ "?id=" + item.getId());		
	}
}
