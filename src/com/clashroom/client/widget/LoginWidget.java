package com.clashroom.client.widget;

import com.clashroom.client.Clashroom;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.LoginInfo;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LoginWidget extends HorizontalPanel {
	private HTML labelLogin;
	
	public LoginWidget(final LoginInfo info) {
		setHorizontalAlignment(ALIGN_RIGHT);
		setWidth("100%");
		labelLogin = new HTML();
		add(labelLogin);
		refreshUrl();
	}

	public void refreshUrl() {
		LoginInfo info = Clashroom.getLoginInfo();
		labelLogin.setHTML(Formatter.format(
				"Logged in as <b>%s</b> (<a href='%s'>Logout</a>)", 
				info.getNickname(), info.getLogoutUrl()));
	}
}
