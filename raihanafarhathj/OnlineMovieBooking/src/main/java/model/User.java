package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import enums.Gender;
import enums.UserStatus;

public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private LocalDate dob;
    private String phoneNumber;
    private Gender gender;
    private UserStatus status;
    private int roleId;
    private LocalDateTime createdAt;

    // No-arg constructor
    public User() {}

    // Constructor for registration
    public User(String name, String email, String password, LocalDate dob,
                String phoneNumber, Gender gender, int roleId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.roleId = roleId;
        this.status = UserStatus.ACTIVE;
    }

    // Getters & Setters

    public int getUserId(){
    	return userId; 
    }
    public void setUserId(int userId){
    	this.userId = userId; 
    }

    public String getName(){
    	return name; 
    }
    public void setName(String name){
    	this.name = name;
    }

    public String getEmail(){
    	return email; 
    }
    public void setEmail(String email){
    	this.email = email; 
    }

    public String getPassword(){
    	return password;
    }
    public void setPassword(String password){
    	this.password = password;
    }

    public LocalDate getDob(){
    	return dob; 
    }
    public void setDob(LocalDate dob){
    	this.dob = dob;
    }

    public String getPhoneNumber(){
    	return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
    	this.phoneNumber = phoneNumber;
    }

    public Gender getGender(){
    	return gender;
    }
    public void setGender(Gender gender){
    	this.gender = gender;
    }

    public UserStatus getStatus(){ 
    	return status;
    }
    public void setStatus(UserStatus status){
    	this.status = status;
    }

    public int getRoleId(){
    	return roleId;
    }
    public void setRoleId(int roleId){
    	this.roleId = roleId;
    }

    public LocalDateTime getCreatedAt(){
    	return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
    	this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "User {" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", roleId=" + roleId +
                ", createdAt=" + createdAt +
                '}';
    }

}
