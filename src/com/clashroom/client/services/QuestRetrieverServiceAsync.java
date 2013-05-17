package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.server.impl.QuestRetrieverServiceImpl;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * See {@link QuestRetrieverServiceImpl}
 */
public interface QuestRetrieverServiceAsync {
	/** See {@link QuestRetrieverServiceImpl#retrieveQuests()} */
    void retrieveQuests(AsyncCallback<ArrayList<QuestEntity>> aCallback);
    /** See {@link QuestRetrieverServiceImpl#retrieveAQuest(long)} */
    void retrieveAQuest(long questId, AsyncCallback<QuestEntity> aCallback);
    /** See {@link QuestRetrieverServiceImpl#addDummyQuest()} */
    void addDummyQuest(AsyncCallback<String> aCallback);//For testing delete later
}
