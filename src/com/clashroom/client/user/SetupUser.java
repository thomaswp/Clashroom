package com.clashroom.client.user;

import com.clashroom.client.Styles;
import com.clashroom.client.resources.MyResources;
import com.clashroom.client.widget.AnimatedProgressBar;
import com.clashroom.client.widget.MomentumScrollPanel;
import com.clashroom.shared.Constant;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.HTML;

public class SetupUser extends Composite {
	
	private static final int SCROLL = 25;
	
	private Label labelEmail;
	private TextBox textBoxFirstName;
	private TextBox textBoxLastName;
	private Button buttonNext;
	private TextBox textBoxUsername;
	private Image imageIcon;
	private DecoratedTabPanel decoratedTabPanel;
	private HorizontalPanel horizontalPanelDragons;
	
	private FocusPanel focusedPanel;
	private DragonClass dragonClass;
	private TextBox textBoxDragonName;
	
	private UserEntity user;
	private Runnable onFinishedHandler;
	
	public SetupUser() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setSize("600px", "auto");
		
		Label lblWelcomeToClashroom = new Label("Welcome to Clashroom!");
		lblWelcomeToClashroom.setStyleName(Styles.page_title);
		verticalPanel.add(lblWelcomeToClashroom);
		
		decoratedTabPanel = new DecoratedTabPanel();
		verticalPanel.add(decoratedTabPanel);
		decoratedTabPanel.setWidth("100%");
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		decoratedTabPanel.add(verticalPanel_1, "Info", false);
		verticalPanel_1.setSize("100%", "3cm");
		
		Label lblItLooksLike = new Label("It looks like you're new here. Why don't you let us know a little bit about yourself.");
		verticalPanel_1.add(lblItLooksLike);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel_1.add(horizontalPanel);
		
		Label lblEmail = new Label("Email: ");
		horizontalPanel.add(lblEmail);
		lblEmail.setStyleName("prompt");
		
		labelEmail = new Label("");
		horizontalPanel.add(labelEmail);
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_1);
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label lblNewLabel = new Label("First Name: ");
		horizontalPanel_1.add(lblNewLabel);
		lblNewLabel.setStyleName("prompt");
		
		KeyPressHandler nextTab = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				int key = event.getNativeEvent().getKeyCode();
				Debug.write(key);
				if (key == KeyCodes.KEY_ENTER) {
					nextTab();
				}
			}
		};
		
		textBoxFirstName = new TextBox();
		horizontalPanel_1.add(textBoxFirstName);
		textBoxFirstName.setMaxLength(30);
		decoratedTabPanel.selectTab(0);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_2);
		horizontalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label lblLastName = new Label("Last Name: ");
		lblLastName.setStyleName("prompt");
		horizontalPanel_2.add(lblLastName);
		
		textBoxLastName = new TextBox();
		textBoxLastName.setMaxLength(30);
		textBoxLastName.addKeyPressHandler(nextTab);
		horizontalPanel_2.add(textBoxLastName);
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		decoratedTabPanel.add(verticalPanel_2, "Player", false);
		verticalPanel_2.setSize("100%", "3cm");
		
		Label lblNowLetsSet = new Label("Now let's set you up a character. Give yourself a name. This is just for other players, so make it something fun or epic!");
		verticalPanel_2.add(lblNowLetsSet);
		
		HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
		horizontalPanel_4.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel_2.add(horizontalPanel_4);
		
		Label lblUsername = new Label("Username:");
		lblUsername.setStyleName("prompt");
		horizontalPanel_4.add(lblUsername);
		
		textBoxUsername = new TextBox();
		textBoxUsername.setMaxLength(30);
		textBoxUsername.addKeyPressHandler(nextTab);
		horizontalPanel_4.add(textBoxUsername);
		
		Button btnRandom = new Button("Random");
		btnRandom.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String names = MyResources.INSTANCE.getNames().getText();
				String[] nameArray = names.split("\t|\n");
				String fisrtName = nameArray[(int)(nameArray.length * Math.random())];
				String lastName = nameArray[(int)(nameArray.length * Math.random())];
				textBoxUsername.setText(fisrtName + " " + lastName);
			}
		});
		horizontalPanel_4.add(btnRandom);
		
		InlineHTML nlnhtmlNewInlinehtml = new InlineHTML("This is your default player icon, but you can change it by setting up an account with your email address on <a href=\"http://www.gravatar.com\" target=\"_blank\">Gravatar</a> (take a while to refresh):");
		verticalPanel_2.add(nlnhtmlNewInlinehtml);
		
		HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
		horizontalPanel_5.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel_2.add(horizontalPanel_5);
		
		imageIcon = new Image("clashroom/clear.cache.gif");
		imageIcon.setStyleName("icon");
		horizontalPanel_5.add(imageIcon);
		
		Button btnRefresh = new Button("Refresh");
		btnRefresh.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				imageIcon.setUrl(imageIcon.getUrl());
			}
		});
		horizontalPanel_5.add(btnRefresh);
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		decoratedTabPanel.add(verticalPanel_3, "Dragon Class", false);
		verticalPanel_3.setSize("100%", "3cm");
		
		Label lblAlrightNowLets = new Label("Alright, now let's set you up with a dragon. Click on a dragon to see more about it. Then, when you've decided, hit the next button.");
		verticalPanel_3.add(lblAlrightNowLets);
		
		HorizontalPanel horizontalPanel1 = new HorizontalPanel();
		horizontalPanel1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel_3.add(horizontalPanel1);
		
		final MomentumScrollPanel scrollPanel = new MomentumScrollPanel();
		scrollPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		
		Button buttonLeft = new Button("<");
		horizontalPanel1.add(buttonLeft);
		buttonLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scrollPanel.scrollHorizontal(-SCROLL);
			}
		});
		buttonLeft.getElement().getStyle().setFloat(Style.Float.LEFT);
		
		horizontalPanel1.add(scrollPanel);
		scrollPanel.setWidth("586px");
		
		horizontalPanelDragons = new HorizontalPanel();
		scrollPanel.setWidget(horizontalPanelDragons);
		horizontalPanelDragons.setSize("100%", "100%");
		
		Button buttonRight = new Button(">");
		buttonRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scrollPanel.scrollHorizontal(SCROLL);
			}
		});
		buttonRight.getElement().getStyle().setFloat(Style.Float.RIGHT);
		horizontalPanel1.add(buttonRight);
		
		final HTML htmlDragonDescription = new HTML("", true);
		verticalPanel_3.add(htmlDragonDescription);
		
		Label lblHp = new Label(Constant.STAT_HP + ":");
		lblHp.setStyleName("prompt");
		verticalPanel_3.add(lblHp);
		lblHp.setWidth("50px");
		
		final AnimatedProgressBar progressBarHp = new AnimatedProgressBar(0.0, 1.0, 0.0);
		verticalPanel_3.add(progressBarHp);
		
		Label lblMp = new Label(Constant.STAT_MP +":");
		lblMp.setStyleName("prompt");
		verticalPanel_3.add(lblMp);
		lblMp.setWidth("50px");
		
		final AnimatedProgressBar progressBarMp = new AnimatedProgressBar(0.0, 1.0, 0.0);
		verticalPanel_3.add(progressBarMp);
		
		Label lblNewLabel_1 = new Label(Constant.STAT_STR + ":");
		lblNewLabel_1.setStyleName("prompt");
		verticalPanel_3.add(lblNewLabel_1);
		lblNewLabel_1.setWidth("50px");
		
		final AnimatedProgressBar progressBarStr = new AnimatedProgressBar(0, 1, 0);
		verticalPanel_3.add(progressBarStr);
		
		Label lblAgility = new Label(Constant.STAT_AGI + ":");
		lblAgility.setStyleName("prompt");
		verticalPanel_3.add(lblAgility);
		lblAgility.setWidth("50px");
		
		final AnimatedProgressBar progressBarAgi = new AnimatedProgressBar(0.0, 1.0, 0.0);
		verticalPanel_3.add(progressBarAgi);
		
		Label lblIntelligence = new Label(Constant.STAT_INT + ":");
		lblIntelligence.setStyleName("prompt");
		verticalPanel_3.add(lblIntelligence);
		lblIntelligence.setWidth("50px");
		
		final AnimatedProgressBar progressBarInt = new AnimatedProgressBar(0.0, 1.0, 0.0);
		verticalPanel_3.add(progressBarInt);
		
		VerticalPanel verticalPanel_4 = new VerticalPanel();
		decoratedTabPanel.add(verticalPanel_4, "Dragon", false);
		verticalPanel_4.setSize("100%", "3cm");
		
		Label lblNowGiveYour = new Label("Great choice! I think it likes you. Do you want to give your dragon a name?");
		verticalPanel_4.add(lblNowGiveYour);
		
		HorizontalPanel horizontalPanel_7 = new HorizontalPanel();
		horizontalPanel_7.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel_4.add(horizontalPanel_7);
		
		Label lblDragonName = new Label("Dragon Name:");
		lblDragonName.setStyleName("prompt");
		horizontalPanel_7.add(lblDragonName);
		
		textBoxDragonName = new TextBox();
		textBoxDragonName.setMaxLength(30);
		textBoxDragonName.addKeyPressHandler(nextTab);
		horizontalPanel_7.add(textBoxDragonName);
		
		HorizontalPanel horizontalPanel_6 = new HorizontalPanel();
		horizontalPanel_6.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_4.add(horizontalPanel_6);
		horizontalPanel_6.setWidth("100%");
		
		final Image imageDragonBig = new Image("clear.cache.gif");
		horizontalPanel_6.add(imageDragonBig);
		
		for (final DragonClass dragonClass : DragonClass.getAllClasses()) {
			Image image = new Image(Constant.IMG_BATTLER + dragonClass.getImageName());
			image.setWidth("150px");
			VerticalPanel vPanel = new VerticalPanel();
			Label label = new Label(dragonClass.getName());
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			vPanel.add(label);
			vPanel.add(image);
			
			final FocusPanel focusPanel = new FocusPanel(vPanel);
			focusPanel.setStyleName("borderHovered");
			focusPanel.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					if (focusedPanel != null) {
						focusedPanel.setStyleName("borderHovered");
					}
					focusedPanel = focusPanel;
					focusPanel.setStyleName("border");
					SetupUser.this.dragonClass = dragonClass; 
					htmlDragonDescription.setHTML(Formatter.format(
							"<b>%s</b>: %s", dragonClass.getName(), 
							dragonClass.getDescription()));
					progressBarHp.animateSetProgress(dragonClass.getHpFactor());
					progressBarMp.animateSetProgress(dragonClass.getMpFactor());
					progressBarStr.animateSetProgress(dragonClass.getStrengthFactor());
					progressBarAgi.animateSetProgress(dragonClass.getAgilityFactor());
					progressBarInt.animateSetProgress(dragonClass.getIntelligenceFactor());
					imageDragonBig.setUrl(Constant.IMG_BATTLER + dragonClass.getImageName());
				}
			});
			
			horizontalPanelDragons.add(focusPanel);
		}
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_3);
		horizontalPanel_3.setWidth("100%");
		
		buttonNext = new Button("Next");
		buttonNext.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				nextTab();
			}
		});
		horizontalPanel_3.add(buttonNext);
		horizontalPanel_3.setCellHorizontalAlignment(buttonNext, HasHorizontalAlignment.ALIGN_RIGHT);
	}
	
	private void nextTab() {
		int index = decoratedTabPanel.getTabBar().getSelectedTab();
		index++;
		if (index < decoratedTabPanel.getTabBar().getTabCount()) {
			decoratedTabPanel.selectTab(index);
		} else {
			tryFinish();
		}
	}
	
	private void tryFinish() {
		if (user == null) return;
		
		String firstName = textBoxFirstName.getText();
		if (firstName.length() == 0) {
			decoratedTabPanel.getTabBar().selectTab(0);
			textBoxFirstName.setFocus(true);
			return;
		}
		user.setFirstName(firstName);
		
		String lastName = textBoxLastName.getText();
		if (lastName.length() == 0) {
			decoratedTabPanel.getTabBar().selectTab(0);
			textBoxLastName.setFocus(true);
			return;
		}
		user.setLastName(lastName);
		
		String username = textBoxUsername.getText();
		if (username.length() == 0) {
			decoratedTabPanel.getTabBar().selectTab(1);
			textBoxUsername.setFocus(true);
			return;
		}
		user.setUsername(username);
		
		DragonEntity dragon = user.getDragon();
		if (dragonClass == null) {
			decoratedTabPanel.getTabBar().selectTab(2);
			return;
		}
		dragon.setDragonClassId(dragonClass.getId());
		
		String dragonName = textBoxDragonName.getText();
		if (dragonName.length() == 0) {
			decoratedTabPanel.getTabBar().selectTab(3);
			textBoxDragonName.setFocus(true);
			return;
		}
		dragon.setName(dragonName);
		
		if (onFinishedHandler != null) {
			onFinishedHandler.run();
		}
	}
	
	public void setUser(UserEntity user) {
		this.user = user;
		getLabelEmail().setText(user.getEmail());
		getImageIcon().setUrl(user.getIconUrl());
	}
	
	public UserEntity getUser() {
		return user;
	}
	
	public void setOnFinishedHandler(Runnable onFinishedHandler) {
		this.onFinishedHandler = onFinishedHandler;
	}

	public Label getLabelEmail() {
		return labelEmail;
	}
	public TextBox getTextBoxFirstName() {
		return textBoxFirstName;
	}
	public TextBox getTextBoxLastName() {
		return textBoxLastName;
	}
	public Button getButtonNext() {
		return buttonNext;
	}
	public TextBox getTextBoxUsername() {
		return textBoxUsername;
	}
	public Image getImageIcon() {
		return imageIcon;
	}
	public TextBox getTextBoxDragonName() {
		return textBoxDragonName;
	}
}
