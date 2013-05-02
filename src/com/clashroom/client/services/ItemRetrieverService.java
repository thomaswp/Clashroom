package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.shared.entity.ItemEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("retrieverItem")
public interface ItemRetrieverService extends RemoteService {
	ArrayList<ItemEntity> retrieveItems();
	ItemEntity retrieveAnItem(long itemId);
	void addItems(); //For testing, maybe delete later unless the game will come with a set or already existing items
}
