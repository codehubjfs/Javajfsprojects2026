package com.appointment.model;

public class Patient {
    private int patientId;
    private int userId;
    private int age;
    private String gender;
    private String phone;
    private String address;
    private User user;

    public Patient() {}

    public int getPatientId() {
    	return patientId;
    	}
    public void setPatientId(int patientId) {
    	this.patientId = patientId; 
    	}    
    public int getUserId() {
    	return userId; 
    	}
    public void setUserId(int userId) {
    	this.userId = userId; 
    	}    
    public int getAge() { 
    	return age; 
    	}
    public void setAge(int age) {
    	this.age = age; 
    	}
    
    public String getGender() {
    	return gender;
    	}
    public void setGender(String gender) 
    { 
    	this.gender = gender; 
    }    
    public String getPhone() {
    	return phone; 
    	}
    public void setPhone(String phone) {
    	this.phone = phone;
    	}
    public String getAddress() { 
    	return address; 
    	}
    public void setAddress(String address) {
    	this.address = address;
    	}
    public User getUser() {
    	return user; 
    	}
    public void setUser(User user) { 
    	this.user = user; 
    	}
}
