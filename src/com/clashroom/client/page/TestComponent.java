package com.clashroom.client.page;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class TestComponent extends Composite {
	private Label labelEmail;
	private TextBox textBoxFirstName;
	private TextBox textBoxLastName;
	private Button buttonNext;
	public TestComponent() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		Label lblWelcomeToClashroom = new Label("Welcome to Clashroom!");
		lblWelcomeToClashroom.setStyleName("headerLabel");
		verticalPanel.add(lblWelcomeToClashroom);
		
		Label lblItLooksLike = new Label("It looks like you're new here. Why don't you let us know a little bit about yourself.");
		verticalPanel.add(lblItLooksLike);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		
		Label lblEmail = new Label("Email: ");
		lblEmail.setStyleName("prompt");
		horizontalPanel.add(lblEmail);
		
		labelEmail = new Label("");
		horizontalPanel.add(labelEmail);
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_1);
		
		Label lblNewLabel = new Label("First Name: ");
		lblNewLabel.setStyleName("prompt");
		horizontalPanel_1.add(lblNewLabel);
		
		textBoxFirstName = new TextBox();
		textBoxFirstName.setMaxLength(30);
		horizontalPanel_1.add(textBoxFirstName);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_2);
		
		Label lblLastName = new Label("Last Name: ");
		lblLastName.setStyleName("prompt");
		horizontalPanel_2.add(lblLastName);
		
		textBoxLastName = new TextBox();
		textBoxLastName.setMaxLength(30);
		horizontalPanel_2.add(textBoxLastName);
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		verticalPanel.add(horizontalPanel_3);
		horizontalPanel_3.setWidth("100%");
		
		buttonNext = new Button("Next");
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
}
