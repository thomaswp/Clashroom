package com.clashroom.client.window;

import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * Represents a Window on the {@link HomePage}, the whole of
 * which can be clicked to navigate to another {@link Page}.
 * Not every slot on the HomePage is actually a Window, as some
 * do not have their whole surface as a click target.
 * 
 * Classes inherting this class should add components to the {@link #focusPanel}
 * and call {@link #initWidget(com.google.gwt.user.client.ui.Widget)} with it
 * at the end if initialization.
 */
public abstract class Window extends Composite implements IWindow {
	
	//used to make the whole window a click target
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
	
	protected void mouseOver(){
		focusPanel.addStyleName(Styles.w_mouseOver);
	}
	
	public abstract void click();

}
