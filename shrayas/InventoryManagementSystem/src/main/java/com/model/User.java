package com.model;

import java.time.LocalDateTime;

import com.enums.Gender;
import com.enums.Status;

public class User {

	private int userId;
	private int roleId;
	private String firstName;
	private String lastName;
	private Gender gender;
	private String email;
	private String password;
	private String phone;
	private Status status;
	private LocalDateTime registeredAt;
	
	public User(String firstName, String lastName, Gender gender, String email, String password, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.roleId = 3;
	}
	
	public User() {
	}

	public int getUserId() {
		return userId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
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
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}
	
	public void setRegisteredAt(LocalDateTime date) {
		this.registeredAt = date;
	}

	public void setUserId(int userId) {
	   this.userId = userId;
	}
	
	public Status getStatus() {
		return status;
	}

}
