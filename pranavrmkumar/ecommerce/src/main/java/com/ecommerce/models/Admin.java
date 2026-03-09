package com.ecommerce.models;

public class Admin extends User{
	public Admin(String name,String email,String role) {
		super(name,email,"admin");
	}
}
