package com.clashroom.client.page;

import com.google.gwt.user.client.ui.Composite;

public abstract class Page extends Composite {

	protected String token;
	
	public String getToken() {
		return token;
	}
	
	public Page(String token) {
		this.token = token;
	}
	
	protected String getParameter(String name) {
		try {
			int q = token.indexOf("?");
			if (q == -1) return null;
			String param = token.substring(q);
			
			int index = param.indexOf(name);
			if (index == -1) return null;
			param = param.substring(index + name.length() + 1);
			
			int and = param.indexOf("&");
			if (and > 0) param = param.substring(0, and);
			
			return param;
		} catch (Exception e) {
			return null;
		}
	}
	
	protected Long getLongParameter(String name) {
		String sParam = getParameter(name);
		try {
			return new Long(sParam);
		} catch (Exception e) {
			return null;
		}
	}

	public void cleanup() { }
}
