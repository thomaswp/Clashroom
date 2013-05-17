package com.clashroom.client.battle;

import com.clashroom.shared.battle.battlers.Battler;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;

/**
 * Not currently implemented.
 * The plan for this Sprite is to serve as a hover-text when
 * mousing over other {@link BattlerSprite}s, Buffs, etc.
 */
public class DetailsSprite {
	
	private float x, y;
	private int width, height;
	private Battler battler;
	
	private final FillStrokeStyle blackStyle = CssColor.make("#000000");
	private final FillStrokeStyle whiteStyle = CssColor.make("#ffffff");
	
	
	public void setBattle(Battler battler) {
		if (battler != this.battler) {
			this.battler = battler;
			width = 150;
			height = 250;
		}
	}
	
	public void setPosition(float x, float y) {
		this.x = x; this.y = y;
	}
	
	public DetailsSprite() {
		
	}
	
	public void update() {
		
	}
	
	public void draw(Context2d context) {
		if (battler != null) {
			context.setFillStyle(whiteStyle);
			context.fillRect(x, y, width, height);
			
			context.setStrokeStyle(blackStyle);
			context.strokeRect(x, y, width, height);
		}
	}
}
