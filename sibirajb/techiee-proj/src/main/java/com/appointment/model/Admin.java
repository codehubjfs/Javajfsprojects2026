
package com.appointment.model;

public class Admin {
    private int adminId;
    private int userId;
    private User user;

    public Admin() {}

    public Admin(int adminId, int userId) {
        this.adminId = adminId;
        this.userId = userId;
    }
    public int getAdminId() {
    	return adminId; 
    	}
    public void setAdminId(int adminId) { 
    	this.adminId = adminId;
    	}
    
    public int getUserId() {
    	return userId; 
    	}
    public void setUserId(int userId) {
    	this.userId = userId; 
    	}  
    public User getUser() { 
    	return user;
    }
    public void setUser(User user) { 
    	this.user = user;
    	}
}