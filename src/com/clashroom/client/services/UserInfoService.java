package com.clashroom.client.services;

import java.util.List;

import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.news.NewsfeedItem;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserInfoService extends RemoteService {
	public UserEntity getUser();
	public UserEntity getUser(long id);
	public void addUser(UserEntity user);
	public void addExp(int exp);
	public List<UserEntity> getAllUsers();
	public void learnSkill(int id); 
	public List<NewsfeedItem> getNews(List<Long> users, int count);
	void completeQuest(long id);
}
