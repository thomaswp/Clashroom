package com.clashroom.client.battle;

import java.util.LinkedList;

import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.services.BattleService;
import com.clashroom.client.services.BattleServiceAsync;
import com.clashroom.client.services.Services;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.Battle;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.actions.ActionDeath;
import com.clashroom.shared.battle.actions.ActionExp;
import com.clashroom.shared.battle.actions.ActionFinish;
import com.clashroom.shared.battle.actions.ActionSkill;
import com.clashroom.shared.battle.actions.ActionSkillTargetAll;
import com.clashroom.shared.battle.actions.BattleAction;
import com.clashroom.shared.battle.actions.ActionSkill.Damage;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.battlers.DragonBattler;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.dragons.DragonHatchling;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.DragonEntity;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A {@link Page} for showing past {@link Battle}s.
 * This page uses HTML5 Canvas to show the battles.
 */
public class BattlePage extends Page implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

	public final static String NAME = "Battle";

	private final BattleServiceAsync battleService = Services.battleService;

	private BattleFactory factory;
	private long entityId;
	
	private Timer timer;

	private Context2d context2d;
	//width and height of the battle scene
	private int width = 1100, height = 600;
	private long lastUpdate;

	private LinkedList<BattlerSprite> battlers = new LinkedList<BattlerSprite>();
	private Battle battle;
	private Label labelInfo, labelTitle;
	private VerticalPanel vPanelInfoHistory;

	private int fps;
	private int fpsFrames;
	private long fpsMS;
	
	private DetailsSprite detailsSprite;
	
	public static String getToken(long id) {
		return NAME + "?id=" + id;
	}

	public static String getHTMLLinkToBattle(String text, long id) {
		return Formatter.format("<a href='#%s?id=%s'>%s</a>", 
				NAME, id, text);
	}
	
	public BattlePage(BattleEntity entity) {
		super(getToken(entity.getId()));
		
		factory = entity.getBattleFactory();
		entityId = entity.getId();
		setupUI();
		setupBattle();
	}

	public BattlePage(long id) {
		super(getToken(id));
		setupUI();
		fetchBattle(id);
	}
	
	public BattlePage(String token) {
		super(token);
		setupUI();
		fetchBattle(getLongParameter("id"));
	}
	
	private void fetchBattle(Long id) {
		if (id != null) {
			entityId = id;

			//fetch the battle from the database
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
		} else {
			//For testing battles and balancing
			
//			testRoundRobbin(1, 10, 30);
//			
//			Battler b1 = createTestBattler("Slick", 0, 20);
//			Battler b2 = createTestBattler("Jax", 1, 20);
//			
//			printBattlerStats(b1);
//			printBattlerStats(b2);
//			factory = createTestFactory(b1, b2);
//			setupBattle();
		}
	}
	
	/**
	 * Prints the stats of a Battler for debugging and
	 * balancing.
	 * @param b The Battler to print
	 */
	@SuppressWarnings("unused")
	private void printBattlerStats(Battler b) {
		System.out.println(Formatter.format(
				"Name: %s\nStr: %s\nAgi: %s\nInt: %s" +
				"\nDodge: %s\nCrit: %s\nSpell: %s\nMelee: %s \n",
				b.name, b.getStrength(), b.getAgility(), b.getIntelligence(),
				b.getDodgeChance(), b.getCriticalChance(), b.getSpellModifier(), b.getMeleeModifier()));
	}
	
	/**
	 * Creates a series of {@link Battle}s for balancing
	 * testing. Prints the results in a table to the console.
	 * Battles are run between each possible pair of {@link DragonClass}s
	 * at every level in the given range. 
	 * <p />
	 * Each row in the printed table corresponds to a DragonBattler,
	 * and each column represents how well it did against each other
	 * DragonBattler. The final column is the win percentage.
	 * 
	 * @param minLevel The level to start testing battles
	 * @param maxLevel The level to end testing battles
	 * @param roundsPerLevel The number of battles to do between
	 * each pair of DragonBattlers at each level
	 */
	@SuppressWarnings("unused")
	private void testRoundRobbin(int minLevel, int maxLevel, int roundsPerLevel) {
		int nDragons = DragonClass.getNumDragonClasses();
		int[][] a = new int[nDragons][nDragons + 1];
		for (int i = 0; i < nDragons; i++) {
			for (int j = 0; j < nDragons; j++) {
				if (i != j) {
					for (int k = minLevel; k <= maxLevel; k++) {
						for (int l = 0; l < roundsPerLevel; l++) {
							Battler b1 = createTestBattler("Test A", i, k);
							Battler b2 = createTestBattler("Test B", j, k);
							
							if (isTeamAWinner(createTestFactory(b1, b2))) {
								a[i][j]++;
								a[i][4]++;
							} else {
								a[j][i]++;
								a[j][4]++;
							}
						}
					}
				}
			}
		}
		
		String out = "";
		int rounds = (nDragons * (nDragons - 1) * (maxLevel - minLevel + 1) * roundsPerLevel);
		int length = ("" + rounds).length();
		for (int i = 0; i < nDragons; i++) {
			out += DragonClass.getById(i).getName().substring(0, 5) + ":\t";
			for (int j = 0; j < nDragons; j++) {
				String n = "" + a[i][j];
				while (n.length() < length) n = " " + n;
				out += (n + ", ");
			}
			out += (float)a[i][nDragons] / rounds + "%\n";
		}
		System.out.println(out);
	}
	
	/**
	 * Runs through a battle to see which team won.
	 * @param factory The BattleFactory to test
	 * @return true if TeamA was the winner of the battle
	 */
	private boolean isTeamAWinner(BattleFactory factory) {
		Battle battle = factory.generateBattle();
		do {
			BattleAction action = battle.nextAction();
			if (action instanceof ActionFinish) {
				if (((ActionFinish) action).teamAVictor) {
					return true;
				} else {
					return false;
				}
			}
		} while (!battle.isOver());
		return false;
	}
	
	/**
	 * Creates a test {@link BattleFactory} pitting the two
	 * provided Dragons against each other
	 */
	private BattleFactory createTestFactory(Battler a, Battler b) {
		LinkedList<Battler> teamA = new LinkedList<Battler>();
		LinkedList<Battler> teamB = new LinkedList<Battler>();
		
		teamA.add(a);
		teamB.add(b);
		
		return new BattleFactory("Team A", teamA, "Team B", teamB);
	}
	
	/**
	 * Creates a {@link DragonBattler} for testing/balancing.
	 * Automatically has the Dragon learn all available skills
	 * at its level.
	 *  
	 * @param name The name of the dragon
	 * @param dragonClass The index of the dragon's class
	 * @param level The level of the dragon
	 * @return The dragon
	 */
	private DragonBattler createTestBattler(String name, int dragonClass, int level) {
		DragonEntity entity = new DragonEntity();
		entity.setName(name);
		entity.setDragonClassId(dragonClass);
		entity.getDragonClass().setUp(entity);
		for (int l = 1; l < level; l++) {
			entity.getDragonClass().levelUp(entity);
		}
		entity.setLevel(level);
		DragonBattler db1 = new DragonBattler(entity, 0);
		for (Skill skill : entity.getDragonClass().getSkillTree().keySet()) {
			if (entity.getLevel() >= entity.getDragonClass().getSkillTree().get(skill)) {
				db1.skills.add(skill);	
			}
		}
		return db1;
	}
	
	@Override
	public void cleanup() {
		if (timer != null) {
			timer.cancel();
		}
	}

	//Set up lots of fun UI
	private void setupUI() {
		Window.setTitle("Battle");

		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName(NAME);
		initWidget(panel);
		
		labelTitle = new Label("Battle");
		labelTitle.addStyleName(Styles.page_title);
		panel.add(labelTitle);
		
		Label instructions = new Label("Click to go!");
		panel.add(instructions);

		HorizontalPanel hPanel = new HorizontalPanel();
		panel.add(hPanel);
		
		Canvas canvas = Canvas.createIfSupported();
		hPanel.add(canvas);

		VerticalPanel vPanelInfo = new VerticalPanel();
		hPanel.add(vPanelInfo);
		
		labelInfo = new Label();
		labelInfo.addStyleName(Styles.battle_info);
		vPanelInfo.add(labelInfo);
		
		vPanelInfoHistory = new VerticalPanel();
		vPanelInfoHistory.addStyleName(Styles.battle_info_history);
		vPanelInfo.add(vPanelInfoHistory);

		
		canvas.setWidth(width + "px");
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);

		//Adding handlers seems to create a performance issue in Java
		//mode, but likely not in javascript
		canvas.addMouseDownHandler(this);
//		canvas.addMouseUpHandler(this);
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
		labelTitle.setText(factory.getName());
		
		vPanelInfoHistory.clear();
		labelInfo.setText("");

		battlers.clear();
		battle = factory.generateBattle();

		//Create team A
		int dx = 40, dy = 65;
		int dSize = battle.getTeamA().size() / 2;
		int x = 230 - dx * dSize, y = height / 2 - dy * dSize;
		for (Battler b : battle.getTeamA()) {
			BattlerSprite bs = new BattlerSprite(b, x, y);
			b.tag = bs;
			battlers.add(bs);
			x += dx; y += dy;
		}

		//Create team B
		dSize = battle.getTeamB().size() / 2;
		x = width - 230 + dx * dSize; 
		y = height / 2 - dy * dSize;
		for (Battler b : battle.getTeamB()) {
			BattlerSprite bs = new BattlerSprite(b, x, y);
			b.tag = bs;
			battlers.add(bs);
			x -= dx; y += dy;
		}
		
		//currently does nothing
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

		//draw the battlers in a batch to save performance
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
		//currently does nothing
		detailsSprite.setPosition(event.getX(), event.getY());
	}
	
	//post a new Action from the battle
	private void postInfo(String text) {
		Label hLabel = new Label(labelInfo.getText());
		hLabel.addStyleName(Styles.battle_info);
		//add the old info to the history
		vPanelInfoHistory.insert(hLabel, 0);
		labelInfo.setText(SafeHtmlUtils.htmlEscape(text));
	}
	
	@Override
	public void onMouseDown(MouseDownEvent event) {
		mouseDown = true;
		BattleAction postAction;
		if (event.getNativeButton() == 2) {
			//Right click resets the battle - mainly for debugging
			setupBattle();
		} else if (battle != null && !battle.isOver()) {
			//advance the battle by one action
			BattleAction action = battle.nextAction();
			if (action instanceof ActionSkill) {
				//If they attack, animate it
				final ActionSkill actionAttack = (ActionSkill) action;
				BattlerSprite attacker = actionAttack.attacker.getTag();
				Runnable damage = null;
				if (!actionAttack.missed) {
					damage = new Runnable() {
						@Override
						public void run() {
							//Delay taking damage until 1/2 through the attack animation
							for (Damage damage : actionAttack.damages) {
								BattlerSprite attacked = damage.target.getTag();
								attacked.takeHit(damage.damage);	
							}
						}
					};
				}
				attacker.attack(damage);
			} else if (action instanceof ActionSkillTargetAll) {
				//We handle target-all attack separately because miss/critical
				//works a little differently. For the purposes of this class,
				//it works out to the same
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
			postInfo(action.toBattleString());
		}
		else if (battle != null && (postAction = battle.getNextPostBattleAction()) != null) {
			//Print experience gains and levels up after the battle ends
			if (postAction instanceof ActionExp) {
				ActionExp actionExp = (ActionExp) postAction;
				if (actionExp.newLevel != actionExp.battler.level) {
					actionExp.battler.setLevel(actionExp.newLevel);
					BattlerSprite sprite = actionExp.battler.getTag();
					sprite.levelUp();
					sprite.setTargetHp(actionExp.battler.getMaxHp());
				}
				((ActionExp) postAction).battler.setLevel(((ActionExp) postAction).newLevel);
			}
			postInfo(postAction.toBattleString());
		}
	}
}
