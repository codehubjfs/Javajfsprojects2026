package model;

import enums.NotificationStatus;
import enums.NotificationType;
import java.time.LocalDateTime;

public class Notification {

    private int _notificationId;
    private NotificationType _notificationType;
    private String _message;
    private NotificationStatus _notificationStatus;
    private LocalDateTime _createdAt;
    private int _userId;

    public Notification() {}

    public Notification(NotificationType notificationType, String message,
                        NotificationStatus notificationStatus, LocalDateTime createdAt, int userId) {
        this._notificationType = notificationType;
        this._message = message;
        this._notificationStatus = notificationStatus;
        this._createdAt = createdAt;
        this._userId = userId;
    }

    // Getters
    public int getNotificationId() {
    	return _notificationId; 
    }
    public NotificationType getNotificationType(){
    	return _notificationType; 
    }
    public String getMessage(){
    	return _message; 
    }
    public NotificationStatus getNotificationStatus(){
    	return _notificationStatus; 
    }
    public LocalDateTime getCreatedAt(){
    	return _createdAt; 
    }
    public int getUserId(){
    	return _userId; 
    }

    // Setters
    public void setNotificationType(NotificationType notificationType) {
    	this._notificationType = notificationType; 
    }
    public void setMessage(String message) {
    	this._message = message; 
    }
    public void setNotificationStatus(NotificationStatus notificationStatus){
    	this._notificationStatus = notificationStatus; 
    }
    public void setCreatedAt(LocalDateTime createdAt){
    	this._createdAt = createdAt; 
    }
    public void setUserId(int userId){ 
    	this._userId = userId; 
    }

    
    public String toString() {
        return "Notification{" +
                "_notificationId=" + _notificationId +
                ", _notificationType=" + _notificationType +
                ", _message='" + _message + '\'' +
                ", _notificationStatus=" + _notificationStatus +
                ", _createdAt=" + _createdAt +
                ", _userId=" + _userId +
                '}';
    }
}
