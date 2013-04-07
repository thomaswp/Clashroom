package com.clashroom.client;

import java.beans.Beans;

import com.google.gwt.user.client.Timer;

public class AnimatedProgressBar extends ProgressBar {
	
	private Timer timer;
	private double desiredProgress;
	private boolean animating;
	
	public AnimatedProgressBar(double minProgress, double maxProgress, double curProgress) {
		super(minProgress, maxProgress, curProgress);
		desiredProgress = curProgress;
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
			if (Math.abs(anim - desiredProgress) < 0.01) anim = desiredProgress;
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
