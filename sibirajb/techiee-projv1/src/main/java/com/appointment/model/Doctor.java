// File: src/main/java/com/appointment/model/Doctor.java
package com.appointment.model;

public class Doctor {
    private int doctorId;
    private int userId;
    private int specializationId;
    private double fees;
    private int experience;
    private String availability;
    private int adminId;
    private User user;
    private Specialization specialization;

    public Doctor() {}

    public int getDoctorId() { 
    	return doctorId;
    	}
    public void setDoctorId(int doctorId) {
    	this.doctorId = doctorId; 
    	}
    public int getUserId() {
    	return userId; 
    	}
    public void setUserId(int userId) { 
    	this.userId = userId;
    	}
    
    public int getSpecializationId() { 
    	return specializationId; 
    	}
    public void setSpecializationId(int specializationId) {
    	this.specializationId = specializationId; 
    }   
    public double getFees() {
    	return fees; 
    	}
    public void setFees(double fees) {
    	this.fees = fees; 
    	}
    
    public int getExperience() {
    	return experience;
    	}
    public void setExperience(int experience) {
    	this.experience = experience;
    	}
    
    public String getAvailability() { 
    	return availability; 
    	}
    public void setAvailability(String availability) { 
    	this.availability = availability; 
    	}
    
    public int getAdminId() { 
    	return adminId;
    	}
    public void setAdminId(int adminId) { 
    	this.adminId = adminId; 
    	}
    
    public User getUser() {
    	return user; 
    	}
    public void setUser(User user) { 
    	this.user = user;
    	}
    
    public Specialization getSpecialization() { 
    	return specialization; 
    	}
    public void setSpecialization(Specialization specialization) {
    	this.specialization = specialization; 
    	}
}