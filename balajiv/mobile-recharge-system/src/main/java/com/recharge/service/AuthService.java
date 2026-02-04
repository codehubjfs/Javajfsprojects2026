package com.recharge.service;

import com.recharge.dao.UserDAO;
import com.recharge.exception.*;
import com.recharge.model.User;
import com.recharge.util.PasswordUtil;

public class AuthService {
	
	private final UserDAO userDAO = new UserDAO();
	
	/**
	 * used to verify login credentials
	 * @param email
	 * @param password
	 * @return
	 * @throws AuthenticationException 
	 */
	public User login(String email, String password) throws AuthenticationException {
		User user = userDAO.findByEmail(email);
		
		if(user == null) {
			throw new AuthenticationException("User not found (register before you login)");
		}
		
		String hashedInput = PasswordUtil.hashPassword(password);
		
		if(!hashedInput.equals(user.getPasswordHash())) {
			throw new AuthenticationException("Invalid email or password");
		}
		
		if(!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
			throw new AuthenticationException("User account is inactive");
		}
		
		return user;
	}
}
