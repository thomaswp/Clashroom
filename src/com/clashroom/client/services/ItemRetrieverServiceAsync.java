package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.server.impl.ItemRetrieverServiceImpl;
import com.clashroom.shared.entity.ItemEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * See {@link ItemRetrieverServiceImpl}
 */
public interface ItemRetrieverServiceAsync {
	/** See {@link ItemRetrieverServiceImpl#retrieveItems()} */
	void retrieveItems(AsyncCallback<ArrayList<ItemEntity>> aCallback);
	/** See {@link ItemRetrieverServiceImpl#retrieveAnItem(long)} */
	void retrieveAnItem(long itemId,AsyncCallback<ItemEntity> aCallback);
	/** See {@link ItemRetrieverServiceImpl#addItems()} */
	void addItems(AsyncCallback<Void> aCallback);
}
