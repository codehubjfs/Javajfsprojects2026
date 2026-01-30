package com.vserv.model;

import java.time.LocalDateTime;

public class Notification {
	private int notificationId;
	private int userId;
	private String notificationType;
	private String title;
	private String message;
	private Integer relatedBookingId;
	private boolean isRead;
	private LocalDateTime sentAt;

	public String getReadStatus() {
		return isRead ? "[READ]" : "[UNREAD]";
	}

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getRelatedBookingId() {
		return relatedBookingId;
	}

	public void setRelatedBookingId(Integer relatedBookingId) {
		this.relatedBookingId = relatedBookingId;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	@Override
	public String toString() {
		return String.format("%s %s - %s", getReadStatus(), title, sentAt);
	}
}