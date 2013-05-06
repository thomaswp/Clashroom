package com.clashroom.client.services;

import java.util.ArrayList;

import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("retrieveQuest")
public interface QuestRetrieverService extends RemoteService {
    ArrayList<QuestEntity> retrieveQuests();
    QuestEntity retrieveAQuest(long questId);
    String addDummyQuest();//For testing delete later
}
