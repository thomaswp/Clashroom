package com.clashroom.client;

import javax.annotation.Nonnull;

import com.clashroom.client.services.LoginService;
import com.clashroom.client.services.LoginServiceAsync;
import com.clashroom.client.user.SetupPage;
import com.clashroom.client.user.SetupUser;
import com.clashroom.shared.LoginInfo;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Clashroom implements EntryPoint, ValueChangeHandler<String> {
	
	private static LoginInfo loginInfo;
	
	public static LoginInfo getLoginInfo() {
		return loginInfo;
	}
	
	private LoginWidget loginWidget;
	
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	
	private static void ensureNotUmbrellaError(@Nonnull Throwable e) {
		for (Throwable th : ((UmbrellaException) e).getCauses()) {
			if (th instanceof UmbrellaException) {
				ensureNotUmbrellaError(th);
			} else {
				th.printStackTrace();
			}
		}
	}
	
	@Override
	public void onModuleLoad() {
	
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(@Nonnull Throwable e) {
				ensureNotUmbrellaError(e);
			}
		});
		
		Window.setTitle("Clashroom");
		
		History.addValueChangeHandler(this);
		
		loginService.login(Window.Location.getHref(), new AsyncCallback<LoginInfo>() {
			
			@Override
			public void onSuccess(LoginInfo result) {
				if (result.isLoggedIn()) {
					loginInfo = result;
					
					RootPanel login = RootPanel.get("login");
					login.add(loginWidget = new LoginWidget(result));
					
					if (result.isHasAccount()) {
						if (History.getToken().isEmpty()) {
							FlowControl.go(new HomePage());
						} else {
					        FlowControl.go(History.getToken());
						}
					} else {
						FlowControl.go(new SetupPage());
					}
				} else {
					Window.Location.replace(result.getLoginUrl());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> e) {
        FlowControl.go(History.getToken());
    }
}
