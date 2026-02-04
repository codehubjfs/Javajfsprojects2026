package com.recharge.service;

import com.recharge.dao.UserDAO;
import com.recharge.model.Role;
import com.recharge.model.User;
import com.recharge.util.PasswordUtil;

public class UserRegistrationService {
	
	private final UserDAO userDAO = new UserDAO();
	
	// allow user to register 
	public void register(String fullName, String gender, String email, String password) {
		if(userDAO.existsByEmail(email)) {
			throw new RuntimeException("Email already registered, Try with new email");
		}
		
		String hashedPassword = PasswordUtil.hashPassword(password);
		User user = new User(0, fullName, gender, email, hashedPassword, Role.USER, "ACTIVE", null);
		
		userDAO.save(user);

        System.out.println("Registration successful. Please login.");
	}
}
