package com.clashroom.client.fragments;

import com.clashroom.client.resources.MyResources;
import com.clashroom.shared.dragons.DragonClass;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;

public class SetupUser extends Composite {
	private Label labelEmail;
	private TextBox textBoxFirstName;
	private TextBox textBoxLastName;
	private Button buttonNext;
	private TextBox textBoxUsername;
	private Image imageIcon;
	private DecoratedTabPanel decoratedTabPanel;
	private HorizontalPanel horizontalPanelDragons;
	public SetupUser() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setSize("600px", "auto");
		
		Label lblWelcomeToClashroom = new Label("Welcome to Clashroom!");
		lblWelcomeToClashroom.setStyleName("headerLabel");
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
		horizontalPanel_4.add(textBoxUsername);
		
		Button btnRandom = new Button("Random");
		btnRandom.addClickHandler(new ClickHandler() {
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
			public void onClick(ClickEvent event) {
				imageIcon.setUrl(imageIcon.getUrl());
			}
		});
		horizontalPanel_5.add(btnRefresh);
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		decoratedTabPanel.add(verticalPanel_3, "Dragon", false);
		verticalPanel_3.setSize("100%", "3cm");
		
		Label lblAlrightNowLets = new Label("Alright, now let's set you up with a dragon. Click on a dragon to see more about it. Then, when you've decided, hit the next button.");
		verticalPanel_3.add(lblAlrightNowLets);
		
		horizontalPanelDragons = new HorizontalPanel();
		verticalPanel_3.add(horizontalPanelDragons);
		for (DragonClass dragonClass : DragonClass.getAllClasses()) {
			Image image = new Image("img/" + dragonClass.getImageName());
			image.setWidth("150px");
			VerticalPanel vPanel = new VerticalPanel();
			Label label = new Label(dragonClass.getName());
			label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vPanel.add(label);
			vPanel.add(image);
			
			FocusPanel focusPanel = new FocusPanel(vPanel);
			focusPanel.setStyleName("borderFocused");
			focusPanel.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					
				}
			});
			
			horizontalPanelDragons.add(focusPanel);
		}
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_3);
		horizontalPanel_3.setWidth("100%");
		
		buttonNext = new Button("Next");
		buttonNext.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int index = decoratedTabPanel.getTabBar().getSelectedTab();
				index++;
				if (index < decoratedTabPanel.getTabBar().getTabCount()) {
					decoratedTabPanel.selectTab(index);
				} else {
					
				}
			}
		});
		horizontalPanel_3.add(buttonNext);
		horizontalPanel_3.setCellHorizontalAlignment(buttonNext, HasHorizontalAlignment.ALIGN_RIGHT);
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
}
