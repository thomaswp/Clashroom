package com.clashroom.client.widget;

import java.beans.Beans;

import com.google.gwt.user.client.Timer;

/**
 * A progress bar, which animates, rather than instantly taking on a value.
 *
 */
public class AnimatedProgressBar extends ProgressBar {

	private Timer timer;
	private double desiredProgress;
	private boolean animating;

	public AnimatedProgressBar() {
		super();
		setup();
	}

	/**
	 * See {@link ProgressBar#ProgressBar(double, double, double)}
	 */
	public AnimatedProgressBar(double minProgress, double maxProgress, double curProgress) {
		super(minProgress, maxProgress, curProgress);
		desiredProgress = curProgress;
		setup();
	}
	
	private void setup() {
		if (!Beans.isDesignTime()) {
			timer = new Timer() {
				@Override
				public void run() {
					updateAnimation();
				}
			};
			timer.scheduleRepeating(1000 / 60);
		}
	}

	private void updateAnimation() {
		if (desiredProgress != getProgress()) {
			double anim = getProgress() * 0.9 + desiredProgress * 0.1;
			double minProg = (getMaxProgress() - getMinProgress()) / 200.0;
			if (Math.abs(anim - desiredProgress) < minProg) anim = desiredProgress;
			animating = true;
			setProgress(anim);
			animating = false;
		}
	}

	/**
	 * Sets progress and animates the display
	 * @param progress
	 */
	public void animateSetProgress(double progress) {
		desiredProgress = progress;
	}

	/**
	 * Sets the progress instantly without animating
	 */
	@Override
	public void setProgress(double progress) {
		super.setProgress(progress);
		if (!animating) {
			desiredProgress = progress;
		}
	}

	@Override
	public void onUnload() {
		super.onUnload();
		//always make sure to stop timers when unloading widgets
		timer.cancel();
	}
}
