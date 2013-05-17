package com.clashroom.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Use this class to retrieve text or image resources programmatically,
 * rather than loading them from a URL.
 */
public interface MyResources extends ClientBundle {

	static final MyResources INSTANCE = GWT.create(MyResources.class);
	
	/**
	 * Gets the list of names for random name creation.
	 * @return The list of names
	 */
	@Source("names.txt")
	public TextResource getNames();
}
