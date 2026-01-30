package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.NotificationDAO;
import com.vserv.model.Notification;

public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public List<Notification> findByUserId(int userId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = """
            select * from notification 
            where user_id = ? 
            ORDER BY sent_at DESC
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notifications.add(mapResultSet(rs));
            }
        }
        return notifications;
    }

    @Override
    public List<Notification> findUnreadByUserId(int userId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = """
            select * from notification 
            where user_id = ? AND is_read = FALSE 
            ORDER BY sent_at DESC
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notifications.add(mapResultSet(rs));
            }
        }
        return notifications;
    }

    @Override
    public Notification insert(Notification notification) throws SQLException {
        String sql = """
            INSERT INTO notification (user_id, notification_type, title, message, related_booking_id)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getNotificationType());
            stmt.setString(3, notification.getTitle());
            stmt.setString(4, notification.getMessage());
            
            if (notification.getRelatedBookingId() != null) {
                stmt.setInt(5, notification.getRelatedBookingId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                notification.setNotificationId(rs.getInt(1));
            }
        }
        return notification;
    }

    @Override
    public void markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE notification SET is_read = TRUE where notification_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void markAllAsRead(int userId) throws SQLException {
        String sql = "UPDATE notification SET is_read = TRUE where user_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public int getUnreadCount(int userId) throws SQLException {
        String sql = "select COUNT(*) from notification where user_id = ? AND is_read = FALSE";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Notification mapResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("notification_id"));
        notification.setUserId(rs.getInt("user_id"));
        notification.setNotificationType(rs.getString("notification_type"));
        notification.setTitle(rs.getString("title"));
        notification.setMessage(rs.getString("message"));
        
        int bookingId = rs.getInt("related_booking_id");
        if (!rs.wasNull()) {
            notification.setRelatedBookingId(bookingId);
        }
        
        notification.setRead(rs.getBoolean("is_read"));
        
        Timestamp sentAt = rs.getTimestamp("sent_at");
        if (sentAt != null) {
            notification.setSentAt(sentAt.toLocalDateTime());
        }
        
        return notification;
    }
}