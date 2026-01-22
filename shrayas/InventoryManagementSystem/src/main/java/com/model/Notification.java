package com.model;

import java.time.LocalDateTime;

import com.enums.NotificationChannel;
import com.enums.NotificationStatus;
import com.enums.NotificationType;

public class Notification {

	private int notificationId;
	private int userId;
	private NotificationType eventType;
	private NotificationChannel channel;
	private String message;
	private NotificationStatus status;
	private LocalDateTime createdAt;
	
	public Notification(int userId, NotificationType eventType, NotificationChannel channel, String message) {
		this.userId = userId;
		this.eventType = eventType;
		this.channel = channel;
		this.message = message;
	}
	
	public Notification() {
	}
	
	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setEventType(NotificationType eventType) {
		this.eventType = eventType;
	}

	public void setChannel(NotificationChannel channel) {
		this.channel = channel;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public NotificationStatus getStatus() {
		return status;
	}
	public void setStatus(NotificationStatus status) {
		this.status = status;
	}

	public int getNotificationId() {
		return notificationId;
	}
	public int getUserId() {
		return userId;
	}
	public NotificationType getEventType() {
		return eventType;
	}
	public NotificationChannel getChannel() {
		return channel;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	
}
