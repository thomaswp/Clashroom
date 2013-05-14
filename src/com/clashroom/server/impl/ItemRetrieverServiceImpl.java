package com.clashroom.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.clashroom.client.services.ItemRetrieverService;
import com.clashroom.server.PMF;
import com.clashroom.server.QueryUtils;
import com.clashroom.shared.battle.skills.AttackSkill;
import com.clashroom.shared.battle.skills.FireBreathSkill;
import com.clashroom.shared.battle.skills.FireballSkill;
import com.clashroom.shared.battle.skills.HealSkill;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.ItemEntity;
import com.clashroom.shared.entity.ItemEntity.ItemType;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ItemRetrieverServiceImpl extends RemoteServiceServlet implements ItemRetrieverService {
	private static final long serialVersionUID = 1L;

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
		return itemList;
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
		return anItemEntity;
	}

	@Override
	public void addItems() {
		List<ItemEntity> itemList = new ArrayList<ItemEntity>();
		Skill fireBall = new FireballSkill();
		Skill fireBreath = new FireBreathSkill();
		Skill heal = new HealSkill();
		Skill attack = new AttackSkill();
    	
    	ItemEntity fireBallScroll = new ItemEntity("Fire Ball Scroll","Reading this scroll will" +
    			" cause your dragon to release a fire ball attack",fireBall,ItemType.ACTIVE,"img");
    	
    	ItemEntity fireBreathScroll = new ItemEntity("Fire Breath Scroll","Reading this scroll will have your dragon release" +
    			" a fire breath attack",fireBreath,ItemType.PASSIVE,"img");
    	
    	ItemEntity healScroll = new ItemEntity("Scroll of Healing","Reading this scroll will" +
    			" heal your dragon",heal,ItemType.PASSIVE,"img");
    	
    	ItemEntity sneakAttack = new ItemEntity("Invisibily Amulet","Using this amulet" +
    			" will allow your dragon to pull off a stealthy sneak attack",attack,ItemType.ACTIVE,"img");
    	
    	itemList.add(fireBallScroll);
    	itemList.add(fireBreathScroll);
    	itemList.add(healScroll);
    	itemList.add(sneakAttack);
		
    	
    	 PersistenceManager pm = PMF.get().getPersistenceManager();
    	 
         try {
        	 pm.makePersistentAll(itemList);
        	 
         } finally {
             pm.close();
         }		
	}
	
}
