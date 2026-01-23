package dao;

import model.Notification;
import exception.DataAccessException;
import java.util.List;

public interface NotificationDAO {
    int addNotification(Notification notification) throws DataAccessException;
    List<Notification> getNotificationsByUser(int userId) throws DataAccessException;
}
