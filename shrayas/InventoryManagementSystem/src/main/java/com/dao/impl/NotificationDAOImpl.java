package com.dao.impl;

import java.sql.*;

import com.dao.NotificationDAO;
import com.enums.NotificationChannel;
import com.enums.NotificationStatus;
import com.enums.NotificationType;
import com.model.Notification;
import com.util.DatabaseConnectionPool;
import java.util.*;

public class NotificationDAOImpl implements NotificationDAO{

	private static final String INSERT = """
	        INSERT INTO notification (user_id, event_type, channel, message)
	        VALUES (?, ?, ?, ?, ?)
	        """;

	    private static final String FIND_BY_USER_ID = """
	        SELECT * FROM notification WHERE user_id = ?
	        ORDER BY created_at DESC
	        """;

	    @Override
	    public int save(Notification notification) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, notification.getUserId());
	            ps.setString(2, notification.getEventType().name());
	            ps.setString(3, notification.getChannel().name());
	            ps.setString(4, notification.getMessage());
	            ps.setString(5, notification.getStatus().name());

	            return ps.executeUpdate();
	        }

	    }

	    @Override
	    public List<Notification> findByUserId(int userId) throws Exception {

	        List<Notification> notifications = new ArrayList<>();

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_USER_ID)) {

	            ps.setInt(1, userId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    notifications.add(mapToNotification(rs));
	                }
	            }
	        }
	        return notifications;
	    }

	    private Notification mapToNotification(ResultSet rs) throws SQLException {

	        Notification notification = new Notification();
	        notification.setNotificationId(rs.getInt("notification_id"));
	        notification.setUserId(rs.getInt("user_id"));
	        notification.setEventType(NotificationType.valueOf(rs.getString("event_type")));
	        notification.setChannel(NotificationChannel.valueOf(rs.getString("channel")));
	        notification.setMessage(rs.getString("message"));
	        notification.setStatus(NotificationStatus.valueOf(rs.getString("status")));
	        notification.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

	        return notification;
	    }
}
