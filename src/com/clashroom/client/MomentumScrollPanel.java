package com.clashroom.client;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ScrollPanel;

public class MomentumScrollPanel extends ScrollPanel implements ScrollHandler {

	private Timer timer;
	private int horizontalMomentum;
	private boolean autoScroll;
	
	public MomentumScrollPanel() {
		super();
		
		timer = new Timer() {
			@Override
			public void run() {
				updateScroll();
			}
		};
		timer.scheduleRepeating(1000 / 60);
		
		addScrollHandler(this);
	}
	
	public void scrollHorizontal(int momentum) {
		horizontalMomentum = momentum;
	}

	@Override
	public void onScroll(ScrollEvent event) {
		if (!autoScroll) {
			horizontalMomentum = 0;
		}
		autoScroll = false;
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		timer.cancel();
	}

	private void updateScroll() { 
		if (horizontalMomentum != 0) {
			autoScroll = true;
			setHorizontalScrollPosition(horizontalMomentum + getHorizontalScrollPosition());
			if (horizontalMomentum > 0) horizontalMomentum--;
			else horizontalMomentum++;
		}
	}
}
