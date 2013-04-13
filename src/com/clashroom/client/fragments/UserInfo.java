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
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.NumberLabel;

public class UserInfo extends Composite {
	
	private UserEntity user;
	private DragonEntity dragon;
	
	public void setUser(UserEntity user) {
		this.user = user;
		Debug.write(user.getDragon());
		dragon = user.getDragon();
		
		DragonClass dragonClass = dragon.getDragonClass();
		
		labelDragonName.setText(dragon.getName());
		imageDragon.setUrl("img/" + dragonClass.getImageName());
		labelLevel.setText("Level " + dragon.getLevel());
		progressBarExp.setMinProgress(0);
		progressBarExp.setMaxProgress(DragonClass.getNextLevelExp(dragon.getLevel()));
		progressBarExp.setProgress(dragon.getExperience());
		
		labelHp.setValue((int)dragon.getMaxHp());
		labelMp.setValue((int)dragon.getMaxMp());
		labelStr.setValue((int)dragon.getStrength());
		labelAgi.setValue((int)dragon.getAgility());
		labelInt.setValue((int)dragon.getIntelligence());
	}
	
	private Label labelDragonName;
	private Image imageDragon;
	private AnimatedProgressBar progressBarExp;
	private Label labelLevel;
	private NumberLabel<Integer> labelHp;
	private NumberLabel<Integer> labelMp;
	private NumberLabel<Integer> labelStr;
	private NumberLabel<Integer> labelAgi;
	private NumberLabel<Integer> labelInt;
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
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_1);
		
		Label lblNewLabel = new Label("HP:");
		horizontalPanel_1.add(lblNewLabel);
		lblNewLabel.setStyleName("prompt");
		
		labelHp = new NumberLabel<Integer>();
		horizontalPanel_1.add(labelHp);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_2);
		
		Label lblMp = new Label("MP:");
		lblMp.setStyleName("prompt");
		horizontalPanel_2.add(lblMp);
		
		labelMp = new NumberLabel<Integer>();
		horizontalPanel_2.add(labelMp);
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_3);
		
		Label lblStrength = new Label("Strength:");
		lblStrength.setStyleName("prompt");
		horizontalPanel_3.add(lblStrength);
		
		labelStr = new NumberLabel<Integer>();
		horizontalPanel_3.add(labelStr);
		
		HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
		horizontalPanel_4.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_4);
		
		Label lblAgility = new Label("Agility:");
		lblAgility.setStyleName("prompt");
		horizontalPanel_4.add(lblAgility);
		
		labelAgi = new NumberLabel<Integer>();
		horizontalPanel_4.add(labelAgi);
		
		HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
		horizontalPanel_5.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_5);
		
		Label lblIntelligence = new Label("Intelligence:");
		lblIntelligence.setStyleName("prompt");
		horizontalPanel_5.add(lblIntelligence);
		
		labelInt = new NumberLabel<Integer>();
		horizontalPanel_5.add(labelInt);
	}

}
