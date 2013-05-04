package com.clashroom.client.battle;

import java.util.LinkedList;

import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.BattleService;
import com.clashroom.client.services.BattleServiceAsync;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battle.Battle;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.actions.ActionDeath;
import com.clashroom.shared.battle.actions.ActionExp;
import com.clashroom.shared.battle.actions.ActionSkill;
import com.clashroom.shared.battle.actions.ActionSkillTargetAll;
import com.clashroom.shared.battle.actions.BattleAction;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.entity.BattleEntity;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BattlePage extends Page implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

	public final static String NAME = "Battle";

	private final BattleServiceAsync battleService = GWT
			.create(BattleService.class);

	private BattleFactory factory;
	private long entityId;
	
	private Timer timer;

	private Context2d context2d;
	private int width = 1000, height = 600;
	private long lastUpdate;

	private LinkedList<BattlerSprite> battlers = new LinkedList<BattlerSprite>();
	private Battle battle;
	private Label info;

	private int fps;
	private int fpsFrames;
	private long fpsMS;
	
	private DetailsSprite detailsSprite;
	
	public static String getToken(long id) {
		return NAME + "?id=" + id;
	}
	
	public BattlePage(BattleEntity entity) {
		super(getToken(entity.getId()));
		
		factory = entity.getBattleFactory();
		entityId = entity.getId();
		setupPage();
		setupBattle();
	}

	public BattlePage(long id) {
		super(getToken(id));
		Debug.write(getToken());
		setupPage();
		fetchBattle(id);
	}
	
	public BattlePage(String token) {
		super(token);
		setupPage();
		fetchBattle(getLongParameter("id"));
	}
	
	private void fetchBattle(Long id) {
		if (id != null) {
			entityId = id;

			battleService.getBattle(entityId, new AsyncCallback<BattleEntity>() {
				@Override
				public void onSuccess(BattleEntity result) {
					BattlePage.this.factory = result.getBattleFactory();
					setupBattle();
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
			});
		}
	}
	
	@Override
	public void cleanup() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private void setupPage() {
		Window.setTitle("Battle");

		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName(NAME);
		initWidget(panel);
		
//		Hyperlink link = new Hyperlink("<", ListBattlePage.NAME);
//		link.addStyleName(Styles.back_button);
//		panel.add(link);

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
//		canvas.addMouseMoveHandler(this);

		context2d = canvas.getContext2d();

		lastUpdate = System.currentTimeMillis();
		timer = new Timer() {
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
		
		detailsSprite = new DetailsSprite();
//		detailsSprite.setBattle(battle.getTeamA().get(0));
	}

	private void update(long timeElapsed) {
		updateFPS(timeElapsed);

		for (BattlerSprite sprite : battlers) {
			sprite.update(timeElapsed);
		}
		detailsSprite.update();
	}

	private void draw() {
		context2d.clearRect(0, 0, width, height);

		BatchedSprite.draw(context2d, BattlerSprite.getRenderer(), battlers);
		detailsSprite.draw(context2d);

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
		detailsSprite.setPosition(event.getX(), event.getY());
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		mouseDown = true;
		BattleAction postAction;
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
		else if (battle != null && (postAction = battle.getNextPostBattleAction()) != null) {
			if (postAction instanceof ActionExp) {
				ActionExp actionExp = (ActionExp) postAction;
				if (actionExp.newLevel != actionExp.battler.level) {
					actionExp.battler.setLevel(actionExp.newLevel);
					BattlerSprite sprite = actionExp.battler.getTag();
					sprite.levelUp();
					sprite.setTargetHp(actionExp.battler.maxHp);
				}
				((ActionExp) postAction).battler.setLevel(((ActionExp) postAction).newLevel);
			}
			info.setText(SafeHtmlUtils.htmlEscape(postAction.toBattleString()));
		}
	}
}
