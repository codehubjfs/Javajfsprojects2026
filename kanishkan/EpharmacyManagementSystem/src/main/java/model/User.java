package model;
import enums.Gender;

import java.time.LocalDateTime;

public class User {

    int userId;
    String firstName;
    String lastName;
    String phoneNumber;
    String email;
    LocalDateTime createdDate;
    String userStatus;
    int addressId;
    int roleId;
    Gender gender;
    String username;
    

    public User(int userId, String firstName, String lastName, String email, int addressId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.addressId = addressId;
    }

    public User(int roleId, String phoneNumber) {
        this.roleId = roleId;
        this.phoneNumber = phoneNumber;
        this.createdDate = LocalDateTime.now();
        this.userStatus = "ACTIVE";
    }
    public User() {
    	
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
