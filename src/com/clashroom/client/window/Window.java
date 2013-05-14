package com.clashroom.client.window;

import com.clashroom.client.Styles;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;


public abstract class Window extends Composite implements IWindow {
	
	protected FocusPanel focusPanel;
	
	public abstract String getName();
	
	public Window(){
		focusPanel = new FocusPanel();
		focusPanel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				mouseOver();
			}
		});
		focusPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				click();
			}
		});
	}
	
	public void mouseOver(){
		focusPanel.addStyleName(Styles.w_mouseOver);
	}
	
	public abstract void click();

}
