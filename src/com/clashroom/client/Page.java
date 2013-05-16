package com.clashroom.client;

import com.clashroom.client.battle.BattlePage;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;

/**
 * The base class for each page in the website.
 * Each page should have a NAME field, which
 * corresponds to the value after the "#" in the
 * URL when accessing that page.
 *
 */
public abstract class Page extends Composite {

	protected String token;
	
	/**
	 * Gets the token that, when passed to {@link FlowControl#go(String)}
	 * will load this page as it was before. The token should consist of 
	 * this Page's NAME, followed by any http-style parameters necessary
	 * to reconstruct it's state. For example "Battle?id=1" would load the
	 * {@link BattlePage} and load a battle with an id of 1.
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Constructs a new Page, using a token. By default, this
	 * should just be a Page's NAME.
	 * @param token The history token for this page, which appears after the "#"
	 * in the URL for this page.
	 */
	public Page(String token) {
		this.token = token;
	}
	
	/**
	 * Extracts an http-style parameter from this page's token.
	 * For example, if this page's token was "NAME?id=0", passing
	 * "id" to this method would return "0".
	 * @param name The name of the parameter to extract
	 * @return The String value of the parameter, or null if it isn't found
	 */
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
	
	/**
	 * Calls {@link Page#getParameter(String)} and attempts to cast
	 * the value to a Long.
	 * @param name The name of the parameter to extract
	 * @return The value, or null if it is not found or could not be
	 * cast to a Long.
	 */
	protected Long getLongParameter(String name) {
		String sParam = getParameter(name);
		try {
			return new Long(sParam);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * A Page must override this method if it
	 * needs to do something before the user navigates
	 * away from it. For instance, if a {@link Timer} is
	 * started on a Page, is should be stopped in this method.
	 */
	public void cleanup() { }
}
