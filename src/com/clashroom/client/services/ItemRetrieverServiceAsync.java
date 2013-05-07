package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.shared.entity.ItemEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ItemRetrieverServiceAsync {
	void retrieveItems(AsyncCallback<ArrayList<ItemEntity>> aCallback);
	void retrieveAnItem(long itemId,AsyncCallback<ItemEntity> aCallback);
	void addItems(AsyncCallback<Void> aCallback);
}
