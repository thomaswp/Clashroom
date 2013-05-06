package com.clashroom.client.widget;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * ScrollPanel which can scroll with momentum, slowly coming
 * to a halt, rather than stopping immediately. The scroll is
 * initiated programmatically, such as at the press of a button.
 * 
 * @author Thomas Price
 */
public class MomentumScrollPanel extends ScrollPanel implements ScrollHandler {

	private Timer timer;
	private int horizontalMomentum;
	
	//Indicates that the next onScroll will be called from, 
	//this class, not caused by the user
	private boolean autoScroll;
	
	public MomentumScrollPanel() {
		super();
		
		//Start a timer to update the scroll
		timer = new Timer() {
			@Override
			public void run() {
				updateScroll();
			}
		};
		timer.scheduleRepeating(1000 / 60); //60 FPS
		
		addScrollHandler(this);
	}
	
	/** Starts a horizontal scroll with momentum **/
	public void scrollHorizontal(int momentum) {
		horizontalMomentum = momentum;
	}

	@Override
	public void onScroll(ScrollEvent event) {
		if (!autoScroll) {
			horizontalMomentum = 0;
		}
		//manual scrolls disable the momentum
		autoScroll = false;
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		timer.cancel();
	}

	private void updateScroll() { 
		if (horizontalMomentum != 0) {
			//set autoScroll before setting the scroll position, so the
			//onScroll method knows it was caused by this method
			autoScroll = true;
			setHorizontalScrollPosition(horizontalMomentum + getHorizontalScrollPosition());
			if (horizontalMomentum > 0) horizontalMomentum--; //linear friction seems to work well
			else horizontalMomentum++;
		}
	}
}