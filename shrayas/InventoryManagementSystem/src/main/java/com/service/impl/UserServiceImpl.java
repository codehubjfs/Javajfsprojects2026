package com.service.impl;

import java.util.List;

import com.dao.impl.UserDAOImpl;
import com.exception.ApplicationException;
import com.dao.UserDAO;
import com.model.User;
import com.service.UserService;

public class UserServiceImpl implements UserService{

	private final UserDAO userDAO = new UserDAOImpl();
	
	public int registerUser(User user) throws Exception {
		
		User exist = userDAO.findUserByEmail(user.getEmail());
		
		if (exist != null) {
			throw new ApplicationException("User with "+user.getEmail()+" already registered");
		}
		
		int rows = userDAO.createUser(user);
		if (rows == 0) {
			throw new ApplicationException("User registration failed");
		}
		
		return rows;
	}

	public User getUserById(int userId) throws Exception {
		
		User user = userDAO.findUserById(userId);
		
		if (user == null) {
			throw new ApplicationException("User not found with the id: "+userId);
		}
		
		return user;
	}

	public User getUserByEmail(String email) throws Exception {
		
		User user = userDAO.findUserByEmail(email);
		
		if (user == null) {
			throw new ApplicationException("User not found with the email: "+email);
		}
		
		return user;
	}

	public List<User> getAllUsers() throws Exception {
		
		List<User> users = userDAO.findAll();
		
		if (users.isEmpty()) {
			throw new ApplicationException("No users Found");
		}
		
		return users;
	}

	public boolean updateUser(User user) throws Exception {
		
		User exist = userDAO.findUserById(user.getUserId());
		
		if (exist == null) {
			throw new ApplicationException("User not exist");
		}
		
		boolean update = userDAO.update(user);
		
		if (!update) {
			throw new ApplicationException("User updation failed");
		}
		
		return true;
	}

	public boolean inactivateUser(int userId) throws Exception {
		
        User exist = userDAO.findUserById(userId);
		
		if (exist == null) {
			throw new ApplicationException("User not exist");
		}
		
		boolean delete = userDAO.delete(userId);
		
		if (!delete) {
			throw new ApplicationException("User incativation failed");
		}
		
		return true;
	}
}
