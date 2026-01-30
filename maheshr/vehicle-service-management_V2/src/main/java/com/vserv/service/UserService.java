package com.vserv.service;

import java.sql.SQLException;

import com.vserv.dao.impl.RoleDAOImpl;
import com.vserv.dao.impl.UserDAOImpl;
import com.vserv.dao.interfaces.RoleDAO;
import com.vserv.dao.interfaces.UserDAO;
import com.vserv.exception.AuthenticationException;
import com.vserv.model.Role;
import com.vserv.model.User;
import com.vserv.util.*;

/**
 *
 * @author Mahesh R
 */
public class UserService {

	private UserDAO userDAO;
	private RoleDAO roleDAO;

	private static final String DEFAULT_ROLE = "CUSTOMER";

	public UserService() {
        this.userDAO = new UserDAOImpl();
        this.roleDAO = new RoleDAOImpl();
	}

	/**
	 * Login
	 * 
	 * @param email
	 * @param password
	 * @return populated user object
	 * @throws AuthenticationException
	 */
	public User login(String email, String password) throws AuthenticationException {
		try {
			User user = userDAO.findByEmail(email);
			validateUserExists(user);
			validateUserStatus(user);
			validatePassword(password, user.getPassword());

			userDAO.updateLastLogin(user.getUserId());
			return user;

		} catch (SQLException e) {
			throw new AuthenticationException("Database error: " + e.getMessage());
		}
	}

	/**
	 * 
	 * @param fullName
	 * @param email
	 * @param password
	 * @param phone
	 * @param gender
	 * @return returns populated user object via userDAO.insert() 
	 * @throws AuthenticationException
	 */
	public User register(String fullName, String email, String password, String phone, String gender)
	        throws AuthenticationException {
	    try {
	        validateEmailNotRegistered(email);

	        final User user = buildUser(fullName, email, PasswordHash.hashPassword(password), phone, gender);
	        assignDefaultRole(user);

	        return userDAO.insert(user);

	    } catch (SQLException e) {
	        throw new AuthenticationException("Registration failed: " + e.getMessage());
	    }
	}

	private void validateUserExists(User user) throws AuthenticationException {
		if (user == null) {
			throw new AuthenticationException("Invalid email or password");
		}
	}

	private void validateUserStatus(User user) throws AuthenticationException {
		if (user.getStatus() == null || !user.getStatus().equals("ACTIVE")) {
			throw new AuthenticationException("Your account has been deactivated. Please contact administrator.");
		}
	}

	private void validatePassword(String inputPassword, String storedPassword) throws AuthenticationException {
	    if (!PasswordHash.verifyPassword(inputPassword, storedPassword)) {
	        throw new AuthenticationException("Invalid email or password");
	    }
	}

	private void validateEmailNotRegistered(String email) throws AuthenticationException, SQLException {
		User existingUser = userDAO.findByEmail(email);
		if (existingUser != null) {
			throw new AuthenticationException("Email already registered");
		}
	}

	/**
	 * 
	 * @param fullName
	 * @param email
	 * @param password
	 * @param phone
	 * @param gender
	 * @return locally built user object
	 */
	private User buildUser(String fullName, String email, String password, String phone, String gender) {
	    User user = new User();
	    user.setFullName(fullName);
	    user.setEmail(email);
	    user.setPassword(password);
	    user.setPhone(phone);
	    user.setGender(gender);
	    return user;
	}

	private void assignDefaultRole(User user) throws AuthenticationException, SQLException {
		Role customerRole = roleDAO.findByName(DEFAULT_ROLE);
		if (customerRole == null) {
			throw new AuthenticationException("System error: Customer role not configured");
		}
		user.setRoleId(customerRole.getRoleId());
	}
}