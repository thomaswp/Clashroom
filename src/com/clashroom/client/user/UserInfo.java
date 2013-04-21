package com.clashroom.client.user;

import com.clashroom.client.Styles;
import com.clashroom.client.widget.AnimatedProgressBar;
import com.clashroom.client.widget.ProgressBar;
import com.clashroom.client.widget.ProgressBar.TextFormatter;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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
		labelLevel.setText(" " + dragon.getLevel());
		progressBarExp.setMinProgress(0);
		progressBarExp.setMaxProgress(DragonClass.getNextLevelExp(dragon.getLevel()));
		progressBarExp.animateSetProgress(dragon.getExperience());
		
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
		initWidget(verticalPanel);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		
		SimplePanel simple = new SimplePanel();
		imageDragon = new Image("clear.cache.gif");
		simple.addStyleName(Styles.profile_portrait);
		simple.add(imageDragon);
		horizontalPanel.add(simple);
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		horizontalPanel.add(verticalPanel_1);
		
		HorizontalPanel hpanel = new HorizontalPanel();
		verticalPanel_1.add(hpanel);
		
		labelDragonName = new Label("Dragon");
		labelDragonName.addStyleName(Styles.profile_name);
		hpanel.add(labelDragonName);
		
		labelLevel = new Label(" 1");
		labelLevel.addStyleName(Styles.profile_lvl);
		hpanel.add(labelLevel);
		
		progressBarExp = new AnimatedProgressBar();
		progressBarExp.setWidth("95%");
		verticalPanel.add(progressBarExp);
		progressBarExp.setTextFormatter(new TextFormatter() {
			@Override
			protected String getText(ProgressBar bar, double curProgress) {
				return Formatter.format("Exp: %s/%s", (int)curProgress, 
						(int)bar.getMaxProgress());
			}
		});
		
		HorizontalPanel horizontalPanel_9 = new HorizontalPanel();
		horizontalPanel_9.addStyleName(Styles.profile_stats);
		verticalPanel_1.add(horizontalPanel_9);
		
		HorizontalPanel horizontalPanel_6 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_6);
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_6.add(horizontalPanel_1);
		
		VerticalPanel vericalPanel_2 = new VerticalPanel();
		horizontalPanel_9.add(vericalPanel_2);
		
		Label lblNewLabel = new Label("HP: ");
		horizontalPanel_1.add(lblNewLabel);
		
		labelHp = new NumberLabel<Integer>();
		horizontalPanel_1.add(labelHp);
		vericalPanel_2.add(horizontalPanel_1);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		vericalPanel_2.add(horizontalPanel_2);
		
		Label lblMp = new Label("MP: ");
		horizontalPanel_2.add(lblMp);
		
		labelMp = new NumberLabel<Integer>();
		horizontalPanel_2.add(labelMp);
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		horizontalPanel_9.add(verticalPanel_3);
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel_3.add(horizontalPanel_3);
		
		Label lblStrength = new Label("Strength: ");
		horizontalPanel_3.add(lblStrength);
		
		labelStr = new NumberLabel<Integer>();
		horizontalPanel_3.add(labelStr);
		
		HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
		verticalPanel_3.add(horizontalPanel_4);

		Label lblAgility = new Label("Agility: ");
		horizontalPanel_4.add(lblAgility);
		
		labelAgi = new NumberLabel<Integer>();
		horizontalPanel_4.add(labelAgi);
		
		HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
		verticalPanel_3.add(horizontalPanel_5);
		
		Label lblIntelligence = new Label("Intelligence: ");
		horizontalPanel_5.add(lblIntelligence);
		
		labelInt = new NumberLabel<Integer>();
		horizontalPanel_5.add(labelInt);
		
		Label skillsLabel = new Label("Skills");
		skillsLabel.addStyleName(Styles.text_title);
		verticalPanel.add(skillsLabel);
	}

}
