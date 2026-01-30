package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.vserv.model.User;

public interface UserDAO {
    User findByEmail(String email) throws SQLException;
    User findById(int userId) throws SQLException;
    List<User> findAll() throws SQLException;
    List<User> findByRole(String roleName) throws SQLException;
    User insert(User user) throws SQLException;
    void updateStatus(int userId, String status) throws SQLException;
    void updateLastLogin(int userId) throws SQLException;
    void delete(int userId) throws SQLException;
    int getTotalCount() throws SQLException;
}