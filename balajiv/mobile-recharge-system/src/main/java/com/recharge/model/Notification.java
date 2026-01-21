package com.recharge.model;

import java.time.LocalDateTime;

public class Notification {

	private int notificationId;
	private int userId;
	private String type;
	private String message;
	private LocalDateTime sendAt;
	
	public Notification(int userId, String type, String message) {
		this.userId = userId;
		this.type = type;
		this.message = message;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
}

