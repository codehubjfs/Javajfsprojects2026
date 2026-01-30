package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.Notification;

public interface NotificationDAO {
    List<Notification> findByUserId(int userId) throws SQLException;
    List<Notification> findUnreadByUserId(int userId) throws SQLException;
    Notification insert(Notification notification) throws SQLException;
    void markAsRead(int notificationId) throws SQLException;
    void markAllAsRead(int userId) throws SQLException;
    int getUnreadCount(int userId) throws SQLException;
}