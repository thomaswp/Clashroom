package com.clashroom.client;

import java.util.LinkedList;

import com.clashroom.shared.GoblinBattler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
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
	private int width = 600, height = 400;
	private long lastUpdate;
	
	private LinkedList<BattlerSprite> battlers = new LinkedList<BattlerSprite>();
	
	private int fps;
	private int fpsFrames;
	private long fpsMS;
	
	public void onModuleLoad() {		
//		Battle battle = new Battle(new GoblinBattler(10), new GoblinBattler(15));
//		while (!battle.isOver()) {
//			System.out.println(battle.nextAction().toBattleString());
//			System.out.println(battle.getStatus());
//		}

		final Canvas canvas = Canvas.createIfSupported();
		RootPanel.get("canvasContainer").add(canvas);
		
		canvas.setWidth(width + "px");
		canvas.setHeight(height + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		
		canvas.addMouseDownHandler(this);
		canvas.addMouseUpHandler(this);
		canvas.addMouseMoveHandler(this);
		
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
		
		bs = new BattlerSprite(new GoblinBattler(10), 150, 150);
		battlers.add(bs);
	}
	
	BattlerSprite bs;
	
	private void update(long timeElapsed) {
		updateFPS(timeElapsed);
		
		for (BattlerSprite sprite : battlers) {
			sprite.update(timeElapsed);
		}
	}
	
	private void draw() {
		context2d.clearRect(0, 0, width, height);
		
		for (BattlerSprite sprite : battlers) {
			sprite.draw(context2d);
		}
		
		drawFPS();
	}
	
	private void drawFPS() {
		String text = fps + "fps";
		context2d.setFillStyle("#000000");
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
		bs.flipped = !bs.flipped;
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		bs.x = event.getX();
		bs.y = event.getY();
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		mouseDown = true;
	}
}
