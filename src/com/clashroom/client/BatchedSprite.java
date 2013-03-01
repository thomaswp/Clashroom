package com.clashroom.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;

public abstract class BatchedSprite {
	protected static abstract class DrawStep<T> {
		public abstract void startStep(Context2d context2d);
		public abstract void doStep(Context2d context2d, T sprite);
		public void endStep(Context2d context2d) { };
	}
	
	protected abstract static class Renderer<T> {
		private ArrayList<DrawStep<T>> drawSteps = 
				new ArrayList<DrawStep<T>>();
		
		protected abstract boolean startDraw(Context2d context2d, T sprite);
		protected abstract void endDraw(Context2d context2d, T sprite);
		protected abstract void addDrawSteps(ArrayList<DrawStep<T>> drawSteps);
		
		public Renderer() {
			addDrawSteps(drawSteps);
		}
	} 
	
	
	@SuppressWarnings("unchecked")
	public static <T extends BatchedSprite> void draw(Context2d context2d, 
			Renderer<T> renderer, Collection<T> sprites) {
		if (sprites.size() == 0) return;
		
		List<DrawStep<T>> drawSteps = renderer.drawSteps;
		for (int i = 0; i < drawSteps.size(); i++) {
			DrawStep<T> sDraw = drawSteps.get(i);
			sDraw.startStep(context2d);
			for (BatchedSprite sprite : sprites) {
				if (renderer.startDraw(context2d, (T)sprite)) {
					sDraw.doStep(context2d, (T)sprite);
					renderer.endDraw(context2d, (T)sprite);
				}
			}
			sDraw.endStep(context2d);
		}
	}
}
