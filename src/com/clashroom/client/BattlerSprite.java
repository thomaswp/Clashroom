package com.clashroom.client;

import com.clashroom.shared.Battler;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;

public class BattlerSprite {
	
	public final static String IMG_DIR = "/img/";
	
	private Battler battler;
	private float width, height;
	private ImageElement image;
	private boolean loaded;
	
	public float x, y;
	public boolean flipped;
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public BattlerSprite(Battler battler, float x, float y) {
		this.battler = battler;
		this.x = x;
		this.y = y;
		
		Image img = new Image(IMG_DIR + battler.image);
		image = ImageElement.as(img.getElement());
	}
	
	public void draw(Context2d context2d) {
		if (width == 0) {
			if (image.getWidth() == 0) return;
			width = image.getWidth();
			height = image.getHeight();
			loaded = true;
		}
		
		
		if (!flipped) {
			context2d.drawImage(image, x - width / 2, y - height / 2);
		} else {
			context2d.save();
			context2d.scale(-1, 1);
			context2d.drawImage(image, -x - width / 2, y - height / 2);
			context2d.restore();
		}
	}
	
	public void update(long timeElapsed) {
		
	}
}
