package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface QuestRetrieverServiceAsync {
    void retrieveQuests(AsyncCallback<ArrayList<QuestEntity>> aCallback);
    void retrieveAQuest(long questId, AsyncCallback<QuestEntity> aCallback);
    void addDummyQuest(AsyncCallback<String> aCallback);//For testing delete later
}
