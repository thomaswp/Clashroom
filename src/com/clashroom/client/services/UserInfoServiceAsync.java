package com.clashroom.client.services;

import java.util.List;

import com.clashroom.server.impl.UserInfoServiceImpl;
import com.clashroom.shared.entity.UserEntity;
import com.clashroom.shared.news.NewsfeedItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * See {@link UserInfoServiceImpl}
 */
public interface UserInfoServiceAsync {
	/** See {@link UserInfoServiceImpl#getUser()} */
	public void getUser(AsyncCallback<UserEntity> callback);
	/** See {@link UserInfoServiceImpl#getUser(long id)} */
	public void getUser(long id, AsyncCallback<UserEntity> callback);
	/** See {@link UserInfoServiceImpl#addUser(UserEntity)} */
	public void addUser(UserEntity user, AsyncCallback<Void> callback);
	/** See {@link UserInfoServiceImpl#getAllUsers()} */
	public void getAllUsers(AsyncCallback<List<UserEntity>> callback);
	/** See {@link UserInfoServiceImpl#addExp(int)} */
	public void addExp(int exp, AsyncCallback<Void> callback);
	/** See {@link UserInfoServiceImpl#learnSkill(int)} */
	public void learnSkill(int id, AsyncCallback<Void> callback);
	/** See {@link UserInfoServiceImpl#getNews(List, int)} */
	public void getNews(List<Long> users, int count, AsyncCallback<List<NewsfeedItem>> callback);
	/** See {@link UserInfoServiceImpl#completeQuest(long)} */
	public void completeQuest(long id, AsyncCallback<Void> callback);
}
