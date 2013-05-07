package com.clashroom.client.window;

import com.clashroom.client.FlowControl;
import com.clashroom.shared.entity.ItemEntity;

public class ItemInnerWindow extends Window {
	
	public final static String NAME = "itemInnerWindow";
	private ItemEntity item;
	
	public ItemInnerWindow(ItemEntity item){
		super();
		this.item = item;
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
