package com.dao;

import java.util.List;

import com.model.Notification;

public interface NotificationDAO {
	
	int save(Notification notification) throws Exception;

    List<Notification> findByUserId(int userId) throws Exception;
}
