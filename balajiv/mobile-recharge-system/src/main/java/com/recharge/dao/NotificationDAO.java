package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.recharge.config.DBConnection;
import com.recharge.model.Notification;

public class NotificationDAO {
	
	private static final String INSERT_NOTIFICATION =
			"""
			insert into notification(user_id, type, message, sent_at)
			values(?, ?, ?, now())
			""";
	
	public void createNotification(Notification notification) {
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_NOTIFICATION);
			
			ps.setInt(1, notification.getUserId());
			ps.setString(2, notification.getType());
			ps.setString(3, notification.getMessage());
			
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to create notification", e);
		}
	}
}
