package com.clashroom.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface MyResources extends ClientBundle {

	static final MyResources INSTANCE = GWT.create(MyResources.class);

	@Source("SetupUser.html")
	public TextResource getIntroHtml();
}
