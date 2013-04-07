package com.clashroom.client.fragments;

import com.clashroom.client.AnimatedProgressBar;
import com.clashroom.client.ProgressBar;
import com.clashroom.client.ProgressBar.TextFormatter;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.data.UserEntity;
import com.clashroom.shared.dragons.DragonClass;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;

public class UserInfo extends Composite {
	
	private UserEntity user;
	private DragonEntity dragon;
	
	public void setUser(UserEntity user) {
		this.user = user;
		Debug.write(user.getDragon());
		dragon = user.getDragon();
		
		DragonClass dragonClass = dragon.getDragonClass();
		
		labelDragonName.setText(dragon.getName());
		imageDragon.setUrl("img/" + dragon.getDragonClass().getImageName());
		labelLevel.setText("Level " + dragon.getLevel());
		progressBarExp.setMinProgress(0);
		progressBarExp.setMaxProgress(DragonClass.getNextLevelExp(dragon.getLevel()));
		progressBarExp.setProgress(dragon.getExperience());
		
	}
	
	private Label labelDragonName;
	private Image imageDragon;
	private AnimatedProgressBar progressBarExp;
	private Label labelLevel;
	public UserInfo() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setStyleName("gwt-vPanel");
		initWidget(verticalPanel);
		verticalPanel.setWidth("600px");
		
		labelDragonName = new Label("Dragon");
		labelDragonName.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		labelDragonName.setStyleName("headerLabel");
		verticalPanel.add(labelDragonName);
		
		labelLevel = new Label("Level 1");
		labelLevel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(labelLevel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("100%");
		
		imageDragon = new Image("clear.cache.gif");
		horizontalPanel.add(imageDragon);
		
		progressBarExp = new AnimatedProgressBar();
		progressBarExp.setTextFormatter(new TextFormatter() {
			@Override
			protected String getText(ProgressBar bar, double curProgress) {
				return Formatter.format("Exp: %s/%s", (int)curProgress, 
						(int)bar.getMaxProgress());
			}
		});
		verticalPanel.add(progressBarExp);
		progressBarExp.setWidth("75%");
	}

}
