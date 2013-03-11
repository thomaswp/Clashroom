package com.clashroom.client.page;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.clashroom.client.BatchedSprite;
import com.clashroom.client.BattleService;
import com.clashroom.client.BattleServiceAsync;
import com.clashroom.client.BattlerSprite;
import com.clashroom.client.Clashroom;
import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.Debug;
import com.clashroom.shared.actions.ActionDeath;
import com.clashroom.shared.actions.ActionSkill;
import com.clashroom.shared.actions.ActionSkillTargetAll;
import com.clashroom.shared.actions.BattleAction;
import com.clashroom.shared.actions.ActionSkill.Damage;
import com.clashroom.shared.battlers.Battler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BattlePage extends Page implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

	public final static String NAME = "Battle";

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
	private long factoryId;

	private Context2d context2d;
	private int width = 1000, height = 600;
	private long lastUpdate;

	private LinkedList<BattlerSprite> battlers = new LinkedList<BattlerSprite>();
	private Battle battle;
	private Label info;

	private int fps;
	private int fpsFrames;
	private long fpsMS;

	public BattlePage(BattleFactory factory) {
		super(NAME + "?id=" + factory.getId());
		
		this.factory = factory;
		factoryId = factory.getId();
		setupPage();
		setupBattle();
	}

	public BattlePage(String token) {
		super(token);
		
		setupPage();
		
		Long id = getLongParameter("id");
		if (id != null) {
			factoryId = id;

			battleService.getBattle(factoryId, new AsyncCallback<BattleFactory>() {
				@Override
				public void onSuccess(BattleFactory result) {
					BattlePage.this.factory = result;
					setupBattle();
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	private void setupPage() {
		Window.setTitle("Battle");

		VerticalPanel panel = new VerticalPanel();
		initWidget(panel);

		info = new Label();
		panel.add(info);

		Canvas canvas = Canvas.createIfSupported();
		panel.add(canvas);

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
	}

	private void setupBattle() {
		if (factory == null) return;
		
		Window.setTitle("Battle - " + factory.getName());

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
			info.setText(SafeHtmlUtils.htmlEscape(action.toBattleString()));
		}
	}
}
