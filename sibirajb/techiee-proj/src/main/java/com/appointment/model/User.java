
package com.appointment.model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String name;
    private String mobileNumber;
    private String role;
    private Timestamp createdAt;

    public User() {}

    public User(int userId, String name, String mobileNumber, String role) {
        this.userId = userId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() {
    	return userId;
    	}
    public void setUserId(int userId) {
    	this.userId = userId; 
    	}
    public String getName() { 
    	return name; 
    	}
    public void setName(String name) {
    	this.name = name; 
    	}
    public String getMobileNumber() {
    	return mobileNumber; 
    	}
    public void setMobileNumber(String mobileNumber) { 
    	this.mobileNumber = mobileNumber; 
    	}
    
    public String getRole() {
    	return role; 
    	}
    public void setRole(String role) {
    	this.role = role; 
    	}
    
    public Timestamp getCreatedAt() { 
    	return createdAt; 
    	}
    public void setCreatedAt(Timestamp createdAt) { 
    	this.createdAt = createdAt; 
    	}

	public int getUserId1() {
		// TODO Auto-generated method stub
		return 0;
	}
}