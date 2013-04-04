package com.clashroom.client.fragments;

import com.clashroom.shared.dragons.DragonClass;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class DragonSelector extends Composite {
	public DragonSelector(DragonClass dragonClass) {
		
		final VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		Label labelName = new Label(dragonClass.getName());
		verticalPanel.add(labelName);
		
		Image image = new Image(dragonClass.getImageName());
		image.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
		image.setWidth("150px");
		verticalPanel.add(image);
	}

}
