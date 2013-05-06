package com.clashroom.client.window;

import com.clashroom.client.FlowControl;
import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.UserInfoService;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.shared.Constant;
import com.clashroom.client.widget.AnimatedProgressBar;
import com.clashroom.client.widget.ProgressBar;
import com.clashroom.client.widget.ProgressBar.TextFormatter;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserInfoWindow extends Window {
	
	private static UserInfoServiceAsync userInfoService = 
			GWT.create(UserInfoService.class);
	
	private UserEntity user;
	private Image image;
	private Label labelDragonName;
	private Label labelLevel;
	private Label userName;
	private AnimatedProgressBar progressBarExp;
	private NumberLabel<Integer> labelHp;
	private NumberLabel<Integer> labelMp;
	private NumberLabel<Integer> labelStr;
	private NumberLabel<Integer> labelAgi;
	private NumberLabel<Integer> labelInt;
	
	public UserInfoWindow() {
		super();
		setupUI();
		
		userInfoService.getUser(new AsyncCallback<UserEntity>() {
			@Override
			public void onSuccess(UserEntity result) {
				user = result;
				populate();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	private void setupUI() {
		VerticalPanel panel = new VerticalPanel();
//		Hyperlink link = new Hyperlink("<", HomePage.NAME);
//		link.addStyleName(Styles.back_button);
//		panel.add(link);

		//panel.addStyleName(UserInfoPage.NAME);
		
		panel.addStyleName(Styles.profile_window);
		
		SimplePanel simple = new SimplePanel();
		simple.addStyleName(Styles.profile_portrait_window);
		image = new Image();
		simple.add(image);
		panel.add(simple);
		
		labelDragonName = new Label();
		labelDragonName.addStyleName(Styles.text_title);
		panel.add(labelDragonName);
		
		labelLevel = new Label(" 1");
		labelLevel.addStyleName(Styles.profile_lvl);
		panel.add(labelLevel);
		
		userName = new Label();
		userName.addStyleName(Styles.text_subtitle);
		panel.add(userName);
		
		progressBarExp = new AnimatedProgressBar();
		progressBarExp.setWidth("95%");
		panel.add(progressBarExp);
		progressBarExp.setTextFormatter(new TextFormatter() {
			@Override
			protected String getText(ProgressBar bar, double curProgress) {
				return Formatter.format("%s: %s/%s", Constant.TERM_EXP_SHORT, 
						(int)curProgress, (int)bar.getMaxProgress());
			}
		});
		
		HorizontalPanel horizontalPanel_9 = new HorizontalPanel();
		horizontalPanel_9.addStyleName(Styles.profile_stats);
		panel.add(horizontalPanel_9);
		
		HorizontalPanel horizontalPanel_6 = new HorizontalPanel();
		panel.add(horizontalPanel_6);
		
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
		
		focusPanel.add(panel);
		initWidget(focusPanel);
	}
	
	
	private void populate() {
		image.setUrl(Constant.IMG_BATTLER + user.getDragon().getDragonClass().getImageName());
		labelDragonName.setText(user.getDragon().getName());
		userName.setText(user.getUsername());
		labelLevel.setText("" + user.getDragon().getLevel());
		progressBarExp.setMinProgress(0);
		progressBarExp.setMaxProgress(DragonClass.getNextLevelExp(user.getDragon().getLevel()));
		progressBarExp.animateSetProgress(user.getDragon().getExperience());
		
		labelHp.setValue((int)user.getDragon().getMaxHp());
		labelMp.setValue((int)user.getDragon().getMaxMp());
		labelStr.setValue((int)user.getDragon().getStrength());
		labelAgi.setValue((int)user.getDragon().getAgility());
		labelInt.setValue((int)user.getDragon().getIntelligence());
	}

	@Override
	public String getName() {
		return "Profile";
	}

	@Override
	public void click() {
		FlowControl.go(new UserInfoPage());
	}

}
