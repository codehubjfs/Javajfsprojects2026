package dao.impl;

import dao.NotificationDAO;
import model.Notification;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import enums.NotificationStatus;
import enums.NotificationType;

public class NotificationDAOImpl implements NotificationDAO {

    @Override
    public int addNotification(Notification notification) throws DataAccessException {

        String sql = """
            INSERT INTO notification (notification_type, message, status, created_at, user_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, notification.getNotificationType().name());
            ps.setString(2, notification.getMessage());
            ps.setString(3, notification.getNotificationStatus() != null 
                         ? notification.getNotificationStatus().name() 
                         : NotificationStatus.SENT.name());
            ps.setTimestamp(4, Timestamp.valueOf(notification.getCreatedAt() != null 
                                                 ? notification.getCreatedAt() 
                                                 : LocalDateTime.now()));
            ps.setInt(5, notification.getUserId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // returns generated notification_id
                }
            }

            throw new DataAccessException("Failed to generate notification ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error adding notification", e);
        }
    }

    @Override
    public List<Notification> getNotificationsByUser(int userId) throws DataAccessException {

        String sql = """
            SELECT * FROM notification
            WHERE user_id = ?
            ORDER BY created_at DESC
        """;

        List<Notification> notifications = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }

            return notifications;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching notifications for user", e);
        }
    }

    // ---------- Helper method ----------
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {

        Notification notification = new Notification(
                NotificationType.valueOf(rs.getString("notification_type")),
                rs.getString("message"),
                NotificationStatus.valueOf(rs.getString("status")),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getInt("user_id")
        );

        // set auto-increment ID internally
        try {
            java.lang.reflect.Field field = Notification.class.getDeclaredField("_notificationId");
            field.setAccessible(true);
            field.set(notification, rs.getInt("notification_id"));
        } catch (Exception ignored) {}

        return notification;
    }
}
