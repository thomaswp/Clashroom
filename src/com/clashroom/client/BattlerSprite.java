package com.clashroom.client;

import java.util.ArrayList;
import java.util.List;

import com.clashroom.shared.Battler;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;

public class BattlerSprite extends BatchedSprite {
	
	public final static String IMG_DIR = "/img/";
	
	private final static int ATTACK_TIME = 400;
	private final static int HURT_TIME = 400;
	private final static float ATTACK_DISTANCE = 40;
	
	private Battler battler;
	private float width, height;
	private ImageElement image, imageTinted;
	private boolean loaded;
	private int hp;
	private int targetHp;
	private double phaseOut;
	private boolean dead;
	
	private int attackingFor = -1;
	private float attackOffset;
	private Runnable onAttackFinishedCallback;
	
	private int hurtFor = -1;
	
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
		super();
		this.battler = battler;
		this.x = x;
		this.y = y;
		this.flipped = !battler.teamA;
		
		targetHp = hp = battler.hp;
		
		Image img = new Image(IMG_DIR + battler.image);
		image = ImageElement.as(img.getElement());
		
		String parts[] = battler.image.split("\\.");
		String imgTint = parts[0] + "-tint." + parts[1];
		img = new Image(IMG_DIR + imgTint);
		imageTinted = ImageElement.as(img.getElement());
	}
	
	public void attack(Runnable onFinishedCallback) {
		onAttackFinishedCallback = onFinishedCallback;
		attackingFor = 0;
	}
	
	public void takeHit() {
		targetHp = battler.hp;
		hurtFor = 0;
	}
	
	public void die() {
		dead = true;
	}
	
	public void update(long timeElapsed) {
		if (width == 0) {
			if (image.getWidth() == 0 || imageTinted.getWidth() == 0) return;
			width = image.getWidth();
			height = image.getHeight();
			loaded = true;
		}
		
		if (attackingFor >= 0) {
			attackingFor += timeElapsed;
			if (attackingFor >= ATTACK_TIME) {
				attackingFor = -1;
				x -= attackOffset;
				attackOffset = 0;
			} else {
				float newOffset = ATTACK_DISTANCE * (float)Math.sin(
						Math.pow((float)attackingFor / ATTACK_TIME, 0.7) * Math.PI );
				int dir = battler.teamA ? 1 : -1;
				newOffset *= dir;
				float dis = newOffset - attackOffset;
				if (dis * dir < 0) {
					if (onAttackFinishedCallback != null) {
						onAttackFinishedCallback.run();
						onAttackFinishedCallback = null;
					}
				}
				x += dis;
				attackOffset = newOffset;
			}
					
		}
		
		if (hurtFor >= 0) {
			hurtFor += timeElapsed;
			if (hurtFor >= HURT_TIME) {
				hurtFor = -1;
			}
		}
		
		int dif = targetHp - hp;
		int seg = Math.max((int)(battler.maxHP * 0.01f), 1);
		if (dif > 0) {
			hp = Math.min(targetHp, hp + seg);
		} else if (dif < 0) {
			hp = Math.max(targetHp, hp - seg);
		}
		
		if (dead && phaseOut < 1) {
			phaseOut += timeElapsed / 1000.0;
		}
	}
	
	public static Renderer<BattlerSprite> getRenderer() {
		return renderer;
	}
	
	private static Renderer<BattlerSprite> renderer;
	static {
		renderer = new Renderer<BattlerSprite>() {

			private boolean lastFlipped;
//			
//			private float left, top, width, height, x, y;
//			private boolean flipped;
//			private Battler battler;
			
			@Override
			protected boolean startDraw(Context2d context2d, BattlerSprite sprite) {
				if (!sprite.loaded) return false;
				
				if (sprite.phaseOut > 1) {
					return false;
				} else if (sprite.phaseOut != 0) {
					context2d.setGlobalAlpha(1 - sprite.phaseOut);
				}
				
				return true;
			}			

			@Override
			protected void endDraw(Context2d context2d, BattlerSprite sprite) {		
				if (context2d.getGlobalAlpha() != 1) {
					context2d.setGlobalAlpha(1);
				}
			}

			@Override
			protected void addDrawSteps(
					ArrayList<DrawStep<BattlerSprite>> drawSteps) {
				drawSteps.add(new DrawStep<BattlerSprite>() {
					@Override
					public void startStep(Context2d context2d) {
						
					}
					
					@Override
					public void doStep(Context2d context2d, BattlerSprite sprite) {
						float left = sprite.x - sprite.width / 2; 
						float top = sprite.y - sprite.height / 2;
						
						if (!sprite.flipped && lastFlipped) {
							context2d.restore();
						} else if (sprite.flipped && !lastFlipped) {
							context2d.save();
							context2d.scale(-1, 1);
						}
						lastFlipped = sprite.flipped;
						
						
						if (!sprite.flipped) {
							context2d.drawImage(sprite.image, left, top);
						} else {
							context2d.drawImage(sprite.image, -sprite.x - sprite.width / 2, top);
						}
						if (sprite.hurtFor >= 0) {
							double alpha = context2d.getGlobalAlpha();
							double perc = Math.sin(Math.PI * sprite.hurtFor / HURT_TIME);
							context2d.setGlobalAlpha(alpha * perc);
							if (!sprite.flipped) {
								context2d.drawImage(sprite.imageTinted, left, top);
							} else {
								context2d.drawImage(sprite.imageTinted, -sprite.x - sprite.width / 2, top);
							}
							context2d.setGlobalAlpha(alpha);
						}
					}
					
					@Override
					public void endStep(Context2d context2d) {
						if (lastFlipped) {
							lastFlipped = false;
							context2d.restore();
						}
					}
				});
				
				final int barHeight = 12;
				drawSteps.add(new DrawStep<BattlerSprite>() {
					final FillStrokeStyle fillStyleA = CssColor.make("#00cc00");
					final FillStrokeStyle fillStyleB = CssColor.make("#ff0000");
					FillStrokeStyle currentFillStyle;
					
					@Override
					public void startStep(Context2d context2d) {
						context2d.setStrokeStyle("#000000");
						context2d.setLineWidth(1);
						currentFillStyle = null;
					}
					
					@Override
					public void doStep(Context2d context2d, BattlerSprite sprite) {
						FillStrokeStyle style = sprite.battler.teamA ? fillStyleA : fillStyleB;
						if (currentFillStyle != style) {
							context2d.setFillStyle(style);
							currentFillStyle = style;
						}
						if (sprite.battler.maxHP > 0) {
							float top = sprite.y - sprite.height / 2;
							int barWidth = (int)(sprite.width * 0.8f);
							int barFill = barWidth * sprite.hp / sprite.battler.maxHP;
							context2d.fillRect(sprite.x - barWidth / 2, top - barHeight * 2, barFill, barHeight);
							context2d.strokeRect(sprite.x - barWidth / 2, top - barHeight * 2, barWidth, barHeight);
						}
					}
				});
				
				drawSteps.add(new DrawStep<BattlerSprite>() {
					final int textSize = 11;
					final FillStrokeStyle fillStyle = CssColor.make("#000000");
					final String font = textSize + "px Book Antiqua";
					
					@Override
					public void startStep(Context2d context2d) {
						context2d.setFillStyle(fillStyle);
						context2d.setFont(font);
						context2d.setTextAlign(TextAlign.CENTER);
					}
					
					@Override
					public void doStep(Context2d context2d, BattlerSprite sprite) {
						float top = sprite.y - sprite.height / 2;
						String status = sprite.hp + "/" + sprite.battler.maxHP;
						context2d.fillText(status, sprite.x, top - barHeight - 2);
					}
				});

				final int titleTextSize = 20;
				drawSteps.add(new DrawStep<BattlerSprite>() {
					
					final FillStrokeStyle fillStyle = CssColor.make("#CCCCCC");
					private String lastString;
					private double lastWidth;
					
					@Override
					public void startStep(Context2d context2d) {
						context2d.setFont(titleTextSize + "px Book Antiqua");
						context2d.setFillStyle(fillStyle);
						
					}
					
					@Override
					public void doStep(Context2d context2d, BattlerSprite sprite) {
						String name = sprite.battler.description;
						double textWidth = lastWidth;
						if (!name.equals(lastString)) {
							textWidth = context2d.measureText(name).getWidth();
							lastString = name;
							lastWidth = textWidth;
						}
						
						double preferredAlpha = (1 - sprite.phaseOut) * 0.5;
						context2d.setGlobalAlpha(preferredAlpha);
						
						int border = 3;
						float top = sprite.y - sprite.height / 2;
						context2d.fillRect(sprite.x - textWidth / 2 - border, 
								top - barHeight * 2 - 3 * titleTextSize / 2 - border + 3,
								textWidth + border * 2, titleTextSize + border * 2);
					}
				});
				
				
				drawSteps.add(new DrawStep<BattlerSprite>() {
					final FillStrokeStyle fillStyle = CssColor.make("#111111");
					
					@Override
					public void startStep(Context2d context2d) {
						context2d.setFillStyle(fillStyle);	
						context2d.setTextAlign(TextAlign.CENTER);
					}
					
					@Override
					public void doStep(Context2d context2d, BattlerSprite sprite) {

						double preferredAlpha = (1 - sprite.phaseOut);
						context2d.setGlobalAlpha(preferredAlpha);
						
						float top = sprite.y - sprite.height / 2;
						context2d.fillText(sprite.battler.description, sprite.x, 
								top - barHeight * 2 - titleTextSize / 2);
					}
				});
			}
		};
	}

}
