package com.clashroom.client;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.Debug;
import com.clashroom.shared.actions.ActionSkill;
import com.clashroom.shared.actions.ActionDeath;
import com.clashroom.shared.actions.ActionSkill.Damage;
import com.clashroom.shared.actions.ActionSkillTargetAll;
import com.clashroom.shared.actions.BattleAction;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.battlers.GoblinBattler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.Link;
import com.google.gwt.dev.Link.LinkOptions;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Clashroom implements EntryPoint, MouseDownHandler, MouseMoveHandler, MouseUpHandler {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final BattleServiceAsync battleService = GWT
			.create(BattleService.class);


	private BattleFactory factory;
	
	private Context2d context2d;
	private int width = 1000, height = 600;
	private long lastUpdate;

	private LinkedList<BattlerSprite> battlers = new LinkedList<BattlerSprite>();
	private Battle battle;
	private Element info;

	private int fps;
	private int fpsFrames;
	private long fpsMS;

	private static void ensureNotUmbrellaError(@Nonnull Throwable e) {
		for (Throwable th : ((UmbrellaException) e).getCauses()) {
			if (th instanceof UmbrellaException) {
				ensureNotUmbrellaError(th);
			} else {
				th.printStackTrace();
			}
		}
	}

	public void onModuleLoad() {		
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(@Nonnull Throwable e) {
				ensureNotUmbrellaError(e);
			}
		});

		info = RootPanel.get("info").getElement();

		final Canvas canvas = Canvas.createIfSupported();
		RootPanel.get("canvasContainer").add(canvas);

		canvas.setWidth(width + "px");
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);

		canvas.addMouseDownHandler(this);
		//canvas.addMouseUpHandler(this);
		//canvas.addMouseMoveHandler(this);

		context2d = canvas.getContext2d();

		lastUpdate = System.currentTimeMillis();
		Timer timer = new Timer() {
			@Override
			public void run() {
				long now = System.currentTimeMillis();
				update(now - lastUpdate);
				lastUpdate = now;

				draw();
			}
		};
		timer.scheduleRepeating(1000 / 60);
		
		battleService.getBattles(new AsyncCallback<List<BattleFactory>>() {
			@Override
			public void onSuccess(List<BattleFactory> result) {
				final FlexTable table = new FlexTable();
				Debug.write(result);
				for (int i = 0; i < result.size(); i++) {
					final BattleFactory factory = result.get(i);
					//table.setHTML(i, 0, factory.getName());
					//Hyperlink link = new Hyperlink(factory.getName(), "battle");
					Button button = new Button(factory.getName());
					button.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							Clashroom.this.factory = factory;
							setupBattle();
						}
					});
					table.setWidget(i, 0, button);
				}
				RootPanel.get("battles").add(table);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
		
//		String idString = Window.Location.getParameter("id");
//		long id;
//		try {
//			id = Long.parseLong(idString);  
//			battleService.getBattle(id, new AsyncCallback<BattleFactory>() {
//				@Override
//				public void onSuccess(BattleFactory result) {
//					factory = result;
//					setupBattle();
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					caught.printStackTrace();
//				}
//			});
//		} catch (Exception e) {
//			
//		}		
	}
	
	private void setupBattle() {
		if (factory == null) return;
		
		battlers.clear();
		battle = factory.generateBattle();
		
		int dx = 40, dy = 65;
		int dSize = battle.getTeamA().size() / 2;
		int x = 200 - dx * dSize, y = height / 2 - dy * dSize;
		for (Battler b : battle.getTeamA()) {
			BattlerSprite bs = new BattlerSprite(b, x, y);
			b.tag = bs;
			battlers.add(bs);
			x += dx; y += dy;
		}

		dSize = battle.getTeamB().size() / 2;
		x = width - 200 + dx * dSize; 
		y = height / 2 - dy * dSize;
		for (Battler b : battle.getTeamB()) {
			BattlerSprite bs = new BattlerSprite(b, x, y);
			b.tag = bs;
			battlers.add(bs);
			x -= dx; y += dy;
		}
	}

	private void update(long timeElapsed) {
		updateFPS(timeElapsed);

		for (BattlerSprite sprite : battlers) {
			sprite.update(timeElapsed);
		}
	}

	private void draw() {
		context2d.clearRect(0, 0, width, height);

		BatchedSprite.draw(context2d, BattlerSprite.getRenderer(), battlers);

		drawFPS();
	}

	private void drawFPS() {
		String text = fps + "fps";
		context2d.setFillStyle("#000000");
		context2d.setFont("16px Arial");
		TextMetrics tm = context2d.measureText(text);
		context2d.fillText(text, width - tm.getWidth() - 2, 12);
	}

	private void updateFPS(long timeElapsed) {
		fpsFrames++;
		fpsMS += timeElapsed;
		while (fpsMS > 1000) {
			fps = fpsFrames;
			fpsMS -= 1000;
			fpsFrames = 0;
		}
	}

	boolean mouseDown;

	@Override
	public void onMouseUp(MouseUpEvent event) {
		mouseDown = false;
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		mouseDown = true;
		if (event.getNativeButton() == 2) {
			setupBattle();
		} else if (battle != null && !battle.isOver()) {
			BattleAction action = battle.nextAction();
			if (action instanceof ActionSkill) {
				final ActionSkill actionAttack = (ActionSkill) action;
				BattlerSprite attacker = actionAttack.attacker.getTag();
				Runnable damage = null;
				if (!actionAttack.missed) {
					damage = new Runnable() {
						@Override
						public void run() {
							for (Damage damage : actionAttack.damages) {
								BattlerSprite attacked = damage.target.getTag();
								attacked.takeHit(damage.damage);	
							}
						}
					};
				}
				attacker.attack(damage);
			} else if (action instanceof ActionSkillTargetAll) {
				final ActionSkillTargetAll actionAttack = (ActionSkillTargetAll) action;
				BattlerSprite attacker = actionAttack.attacker.getTag();
				Runnable damage = new Runnable() {
					@Override
					public void run() {
						for (ActionSkill attack : actionAttack.attacks) {
							if (!attack.missed) {
								Damage damage = attack.getPrimaryDamage();
								BattlerSprite attacked = damage.target.getTag();
								attacked.takeHit(damage.damage);
							}
						}
					}
				};
				attacker.attack(damage);
			} else if (action instanceof ActionDeath) {
				BattlerSprite battler = ((ActionDeath) action).battler.getTag();
				battler.die();
			}
			info.setInnerHTML(SafeHtmlUtils.htmlEscape(action.toBattleString()));
		}
	}
}
