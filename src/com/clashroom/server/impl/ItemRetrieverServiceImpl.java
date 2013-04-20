package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.entity.ItemEntity;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ItemRetrieverServiceImpl extends RemoteServiceServlet implements ItemRetrieverService {

	@Override
	public ArrayList<ItemEntity> retrieveItems() {
		Collection<ItemEntity> itemCollection = null;
		ArrayList<ItemEntity> itemList = new ArrayList<ItemEntity>();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Query q = pm.newQuery("select from " + ItemEntity.class.getName());
			List<ItemEntity> results = (List<ItemEntity>) q.execute();
			itemCollection = pm.detachCopyAll(results);
			
			for(ItemEntity iE : itemCollection){
				itemList.add(iE);
			}
			
		}finally{
			pm.close();
		}
		return null;
	}

	@Override
	public ItemEntity retrieveAnItem(long itemId) {
		ItemEntity anItemEntity = null;
		Collection<ItemEntity> itemCollection = null;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try
		{
			anItemEntity = QueryUtils.queryUnique(pm, ItemEntity.class, "id==%s", itemId);
			anItemEntity = pm.detachCopy(anItemEntity);
		}finally{
			pm.close();
		}
		return null;
	}

}
