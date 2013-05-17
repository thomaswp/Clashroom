package com.clashroom.client;

import javax.annotation.Nonnull;

import com.clashroom.client.services.LoginService;
import com.clashroom.client.services.LoginServiceAsync;
import com.clashroom.client.services.Services;
import com.clashroom.client.user.SetupPage;
import com.clashroom.client.widget.LoginWidget;
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
 * The entry point for the application. Every time the page is refreshed,
 * execution start over at the {@link #onModuleLoad()} method of this class.
 */
public class Clashroom implements EntryPoint, ValueChangeHandler<String> {
	
	private static LoginInfo loginInfo;
	
	/**
	 * Gets the {@link LoginInfo} from the user's last log-in.
	 * Because the application requires a log-in for any action,
	 * it is safe to assume this is set and current after a {@link Page}
	 * has been loaded.
	 */
	public static LoginInfo getLoginInfo() {
		return loginInfo;
	}
	
	private final static LoginServiceAsync loginService = Services.loginService;
	
	/**
	 * Splits an {@link UmbrellaException} into its requisite parts
	 * to print them.
	 */
	private static void ensureNotUmbrellaError(@Nonnull Throwable e) {
		for (Throwable th : ((UmbrellaException) e).getCauses()) {
			if (th instanceof UmbrellaException) {
				ensureNotUmbrellaError(th);
			} else {
				th.printStackTrace();
			}
		}
	}
	
	/**
	 * The entry point for the whole application. Functions a main() method.
	 * Every time the page is refreshed, this application restarts and calls
	 * this method.
	 */
	@Override
	public void onModuleLoad() {
	
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(@Nonnull Throwable e) {
				ensureNotUmbrellaError(e);
			}
		});
		
		//Sets the browser's title
		Window.setTitle("Clashroom");
		
		//Handle "Back" and "Forward" browser events
		History.addValueChangeHandler(this);
		
		//Get the user's login information
		loginService.login(Window.Location.getHref(), new AsyncCallback<LoginInfo>() {
			
			@Override
			public void onSuccess(LoginInfo result) {
				if (result.isLoggedIn()) {
					loginInfo = result;
					
					//Create the "Log Out" widget at the top of the screen
					RootPanel login = RootPanel.get("login");
					login.add(new LoginWidget(result));
					
					if (result.hasAccount()) {
						if (History.getToken().isEmpty()) {
							//By default go to the home page
							FlowControl.go(new HomePage());
						} else {
							//If they have history (likely meaning they've refreshed the page)
							//Direct them to the page from the last History token
					        FlowControl.go(History.getToken());
						}
					} else {
						//If the user doesn't have an account, direct them to create one
						FlowControl.go(new SetupPage());
					}
				} else {
					//If there is no logged in user, redirect them to log in
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
