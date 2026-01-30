package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.UserDAO;
import com.vserv.model.User;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = """
                select u.*, r.role_name 
                from user u 
                JOIN role r ON u.role_id = r.role_id 
                where u.email = ?
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = """
                select u.*, r.role_name 
                from user u 
                JOIN role r ON u.role_id = r.role_id 
                ORDER BY u.user_id
                """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }
    
    @Override
    public User findById(int userId) throws SQLException {
        String sql = """
                select u.*, r.role_name 
                from user u 
                JOIN role r ON u.role_id = r.role_id 
                where u.user_id = ?
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    @Override
    public List<User> findByRole(String roleName) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = """
                select u.*, r.role_name 
                from user u 
                JOIN role r ON u.role_id = r.role_id 
                where r.role_name = ?
                ORDER BY u.user_id
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public User insert(User user) throws SQLException {
        String sql = """
                INSERT INTO user (full_name, email, password, phone, gender, role_id, status)
                VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getGender());
            stmt.setInt(6, user.getRoleId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }
        }
        return user;
    }
    

    @Override
    public void delete(int userId) throws SQLException {
        String sql = "DELETE from user where user_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new SQLException("User not found or already deleted");
            }
        }
    }

    @Override
    public void updateStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE user SET status = ? where user_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE user SET last_login = CURRENT_TIMESTAMP where user_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public int getTotalCount() throws SQLException {
        String sql = "select COUNT(*) from user";

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setGender(rs.getString("gender"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setStatus(rs.getString("status"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        return user;
    }
}