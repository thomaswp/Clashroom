package com.clashroom.client;

import java.beans.Beans;

import com.google.gwt.user.client.Timer;

public class AnimatedProgressBar extends ProgressBar {

	private Timer timer;
	private double desiredProgress;
	private boolean animating;

	public AnimatedProgressBar() {
		super();
		setup();
	}

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

	public void animateSetProgress(double progress) {
		desiredProgress = progress;
	}

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
		timer.cancel();
	}
}
