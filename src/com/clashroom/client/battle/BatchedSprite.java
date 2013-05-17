package com.clashroom.client.battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.clashroom.shared.Constant;
import com.clashroom.shared.battle.buffs.Buff;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;

/** 
 * A Sprite that is drawn in Batches. This allows us to optimize
 * graphics by drawing similar steps together, avoiding changing
 * Canvas properties, like draw/fill color, alpha, and flipping.
 */
public abstract class BatchedSprite {
	/**
	 * One step in the drawing process, such as drawing the
	 * health bar.
	 * @param <T> The kind of Sprite this will draw
	 */
	protected static abstract class DrawStep<T> {
		/**
		 * Starts the step. This is called only once for each
		 * step, so any changes to the canvas' state should be
		 * made here if they are consistent for every Sprite in
		 * the batch.
		 */
		public abstract void startStep(Context2d context2d);
		/**
		 * Called once for each Sprite in the batch. Should draw
		 * this step of the Sprite.
		 * @param context2d The Contex2d to draw onto
		 * @param sprite The sprite to draw
		 */
		public abstract void doStep(Context2d context2d, T sprite);
		/**
		 * Called at the end of a DrawStep to clean up any changes
		 * made to the Canvas' state.
		 */
		public void endStep(Context2d context2d) { };
	}
	
	/**
	 * A renderer for a batched Sprite. This handles the setup, draw and
	 * cleanup for a batch of Sprites.
	 *
	 * @param <T> The kind of Sprite to draw
	 */
	protected abstract static class Renderer<T> {
		private ArrayList<DrawStep<T>> drawSteps = 
				new ArrayList<DrawStep<T>>();
		
		/**
		 * Called for each Sprite before each {@link DrawStep}
		 * @param context2d The Context2d for altering the Canvas' state
		 * @param sprite The sprite
		 * @return true if this Sprite should be drawn
		 */
		protected abstract boolean startDraw(Context2d context2d, T sprite);
		/**
		 * Called for each Sprite after each {@link DrawStep}
		 * @param context2d The Context2d for altering the Canvas' state
		 * @param sprite The sprite
		 */
		protected abstract void endDraw(Context2d context2d, T sprite);
		/**
		 * Called upon creation to create the {@link DrawStep}s for this Renderer.
		 * @param drawSteps
		 */
		protected abstract void addDrawSteps(ArrayList<DrawStep<T>> drawSteps);
		
		public Renderer() {
			addDrawSteps(drawSteps);
		}
		
		
		private HashMap<String, ImageElement> buffMap = new HashMap<String, ImageElement>();
		/**
		 * Loads an ImageElement from the given path. The image is cached for later access.
		 * This is intended for use in drawing {@link Buff}s.
		 * @param name The path to the image
		 * @return The ImageElement for drawing
		 */
		protected ImageElement loadBuffImage(String name) {
			if (buffMap.containsKey(name)) return buffMap.get(name);
			
			ImageElement image = ImageElement.as(new Image(Constant.IMG_ICON + name).getElement());
			buffMap.put(name, image);
			return image;
		}
	} 
	
	/**
	 * Draws this batch of Sprites using the given {@link Renderer}.
	 * @param context2d The Context2d to draw on
	 * @param renderer The Renderer to use
	 * @param sprites The Sprites to draw
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BatchedSprite> void draw(Context2d context2d, 
			Renderer<T> renderer, Collection<T> sprites) {
		if (sprites.size() == 0) return;
		
		//For each DrawStep...
		List<DrawStep<T>> drawSteps = renderer.drawSteps;
		for (int i = 0; i < drawSteps.size(); i++) {
			DrawStep<T> sDraw = drawSteps.get(i);
			//Start the draw step (done once per frame)
			sDraw.startStep(context2d);
			//Then, for each sprite in the batch...
			for (BatchedSprite sprite : sprites) {
				//Start to draw the Sprite
				if (renderer.startDraw(context2d, (T)sprite)) {
					//And if successful, draw it
					sDraw.doStep(context2d, (T)sprite);
				}
				//Then allow the sprite to cleanup
				renderer.endDraw(context2d, (T)sprite);
			}
			//Then end the draw step
			sDraw.endStep(context2d);
		}
	}
}
