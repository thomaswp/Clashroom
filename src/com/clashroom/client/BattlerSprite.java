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
	private int hp;
	private double phaseOut;
	
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
		this.flipped = !battler.teamA;
		
		hp = battler.hp;
		
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
		
		if (phaseOut > 1) {
			return;
		} else if (phaseOut != 0) {
			context2d.setGlobalAlpha(1 - phaseOut);
		}
		
		
		float left = x - width / 2, right = x + width / 2;
		float top = y - height / 2, bot = y + height / 2;
		
		if (!flipped) {
			context2d.drawImage(image, left, top);
		} else {
			context2d.save();
			context2d.scale(-1, 1);
			context2d.drawImage(image, -x - width / 2, top);
			context2d.restore();
		}
		
		if (battler.maxHP > 0) {
			int barHeight = 12;
			int barWidth = (int)(width * 0.8f);
			int barFill = barWidth * hp / battler.maxHP;
			context2d.setFillStyle(battler.teamA ? "#00CC00" : "#ff0000");
			context2d.fillRect(x - barWidth / 2, top - barHeight * 2, barFill, barHeight);
	
			context2d.setStrokeStyle("#000000");
			context2d.setLineWidth(1);
			context2d.strokeRect(x - barWidth / 2, top - barHeight * 2, barWidth, barHeight);
			
			int textSize = 11;
			context2d.setFillStyle("#000000");
			String status = hp + "/" + battler.maxHP;
			context2d.setFont(textSize + "px Book Antiqua");
			double textWidth = context2d.measureText(status).getWidth();
			context2d.fillText(status, x - textWidth / 2, top - barHeight - 2);
			
			textSize = 20;
			context2d.setFont(textSize + "px Book Antiqua");
			String name = battler.description;
			textWidth = context2d.measureText(name).getWidth();
			context2d.setFillStyle("#CCCCCC");
			context2d.setGlobalAlpha((1 - phaseOut) * 0.5);
			int border = 3;
			context2d.fillRect(x - textWidth / 2 - border, 
					top - barHeight * 2 - 3 * textSize / 2 - border + 3,
					textWidth + border * 2, textSize + border * 2);
			context2d.setGlobalAlpha((1 - phaseOut));
			context2d.setFillStyle("#111111");
			context2d.fillText(name, x - textWidth / 2, top - barHeight * 2 - textSize / 2);
		}
		
		if (phaseOut > 0) {
			context2d.setGlobalAlpha(1);
		}
	}
	
	public void update(long timeElapsed) {
		int dif = battler.hp - hp;
		int seg = Math.max((int)(battler.maxHP * 0.01f), 1);
		if (dif > 0) {
			hp = Math.min(battler.hp, hp + seg);
		} else if (dif < 0) {
			hp = Math.max(battler.hp, hp - seg);
		}
		
		if (battler.hp == 0 && phaseOut < 1) {
			phaseOut += timeElapsed / 1000.0;
		}
	}
	
	private int lerp(int x0, int x1, float friction) {
		return Math.round(x0 * friction + x1 * (1 - friction));
	}
}
