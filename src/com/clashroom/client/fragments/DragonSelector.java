package com.clashroom.client.fragments;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DragonSelector extends Composite {
	public DragonSelector() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		Image image = new Image((String) null);
		verticalPanel.add(image);
	}

}
