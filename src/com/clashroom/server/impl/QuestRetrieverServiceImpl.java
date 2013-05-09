package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.clashroom.client.services.QuestRetrieverService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.entity.QuestEntity;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * The server side implementation of the RPC service. This is how I
 * grab quest from the database.
 */
@SuppressWarnings("serial")
public class QuestRetrieverServiceImpl extends RemoteServiceServlet
                                implements QuestRetrieverService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * elon.edu.clashroom.profUI.client.QuestCreatorService#createQuest
     * ()
     */
    @Override
    public ArrayList<QuestEntity> retrieveQuests() {
        Collection<QuestEntity> questCollection = null;
        ArrayList<QuestEntity> questList = new ArrayList<QuestEntity>();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Query q = pm.newQuery("select from " + QuestEntity.class.getName());
            List<QuestEntity> results = (List<QuestEntity>) q.execute();
            questCollection = pm.detachCopyAll(results);
            for (QuestEntity qst : questCollection) {
                questList.add(qst);
            }

        } finally {
            pm.close();
        }

        return questList;
    }
    //TODO: Be sure to change this to ID
	@Override
	public QuestEntity retrieveAQuest(long questId) {
		QuestEntity aQuestEntity = null;
		Collection<QuestEntity> questCollection = null;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			
			aQuestEntity = QueryUtils.queryUnique(pm, QuestEntity.class, "id==%s", questId);
			aQuestEntity = pm.detachCopy(aQuestEntity);
			
		}finally{
			pm.close();
		}
		
		return aQuestEntity;
	}
	
	/*
     * This is a temporary method that will be used to create dummy
     * quests. It will be deleted when the project is near completion.
     */
	@Override
	public String addDummyQuest() {
		List<Long> dummyList = new ArrayList();
		dummyList.add((long) 10);
    	
    	QuestEntity dummyQuest = new QuestEntity("Train your Dragon", "Now that you have a dragon you must train it! Go to Temple Belk to obtain the dragon scroll", "ycmb",
                100, 0,
                "4-21-13 10:37pm",
                "This quest will not expire",
                "Congratulations! You have trained your dragon and managed to teach it a new skill. You should be proud, but do not rejoice for too long for tougher quests await.","",
                dummyList, 100);
    	
    	 PersistenceManager pm = PMF.get().getPersistenceManager();
    	 
         try {
             pm.makePersistent(dummyQuest);
         } finally {
             pm.close();
         }
		return "Dummy Quest Added";	
	}
}
