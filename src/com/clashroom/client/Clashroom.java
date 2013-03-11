package com.clashroom.client;

import javax.annotation.Nonnull;

import com.clashroom.client.page.BattlePage;
import com.clashroom.client.page.SelectBattlePage;
import com.clashroom.shared.Debug;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Clashroom implements EntryPoint, ValueChangeHandler<String> {
	
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
		
		if (History.getToken().isEmpty()) {
			FlowControl.go(new SelectBattlePage());
		} else {
	        FlowControl.go(History.getToken());
		}
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> e) {
        FlowControl.go(History.getToken());
    }
}
