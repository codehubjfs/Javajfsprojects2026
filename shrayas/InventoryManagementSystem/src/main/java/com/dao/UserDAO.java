package com.dao;

import java.util.List;

import com.model.User;

public interface UserDAO {
	
	int createUser(User user) throws Exception;
	
	User findUserById(int userId) throws Exception;
	
	User findUserByEmail(String email) throws Exception;
	
	List<User> findAll() throws Exception;
	
	boolean update(User user) throws Exception;
	
	boolean delete(int userId) throws Exception;
}
