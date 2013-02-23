package com.clashroom.client;

import java.util.LinkedList;

import com.clashroom.shared.Battle;
import com.clashroom.shared.BattleAction;
import com.clashroom.shared.Battler;
import com.clashroom.shared.GoblinBattler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	
	private Context2d context2d;
	private ImageElement image;
	private int width = 1000, height = 600;
	private long lastUpdate;
	
	private LinkedList<BattlerSprite> battlers = new LinkedList<BattlerSprite>();
	private Battle battle;
	private Element info;
	
	private int fps;
	private int fpsFrames;
	private long fpsMS;
	
	public void onModuleLoad() {		

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
		image = ImageElement.as(new Image("/img/goblin.png").getElement());
		
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
		
		LinkedList<Battler> teamA = new LinkedList<Battler>();
		LinkedList<Battler> teamB = new LinkedList<Battler>();
		teamA.add(new GoblinBattler(10));
		teamA.add(new GoblinBattler(12));
		teamA.add(new GoblinBattler(13));
		teamB.add(new GoblinBattler(15));
		teamB.add(new GoblinBattler(8));
		teamB.add(new GoblinBattler(12));
		battle = new Battle(teamA, teamB);
//		while (!battle.isOver()) {
//			System.out.println(battle.nextAction().toBattleString());
//			System.out.println(battle.getStatus());
//		}
		
		int dx = 40, dy = 65;
		int dSize = battle.getTeamA().size() / 2;
		int x = 200 - dx * dSize, y = height / 2 - dy * dSize;
		for (Battler b : battle.getTeamA()) {
			BattlerSprite bs = new BattlerSprite(b, x, y);
			battlers.add(bs);
			x += dx; y += dy;
		}
		
		dSize = battle.getTeamB().size() / 2;
		x = width - 200 + dx * dSize; 
		y = height / 2 - dy * dSize;
		for (Battler b : battle.getTeamB()) {
			BattlerSprite bs = new BattlerSprite(b, x, y);
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
		BattleAction action = battle.nextAction();
		info.setInnerHTML(SafeHtmlUtils.htmlEscape(action.toBattleString()));
	}
}
