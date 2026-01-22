package com.service;

import java.util.List;

import com.model.User;

public interface UserService {

	int registerUser(User user) throws Exception;

    User getUserById(int userId) throws Exception;

    User getUserByEmail(String email) throws Exception;

    List<User> getAllUsers() throws Exception;

    boolean updateUser(User user) throws Exception;

    boolean inactivateUser(int userId) throws Exception;
}
