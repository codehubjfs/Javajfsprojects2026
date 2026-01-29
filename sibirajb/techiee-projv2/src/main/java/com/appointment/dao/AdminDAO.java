package com.appointment.dao;

import com.appointment.model.Admin;
import com.appointment.util.DBConnection;

import java.sql.*;

public class AdminDAO {
    public Admin findByUserId(int userId) {
        String sql = "SELECT * FROM admin WHERE user_id = ?";   
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {       
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
            	return new Admin(rs.getInt("admin_id"), rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error finding admin: " + e.getMessage());
        }
        return null;
    }
    public int createAdmin(int userId) {
        String sql = "INSERT INTO admin (user_id) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 
            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating admin: " + e.getMessage());
        }
        return -1;
    }
    /*
     * Find admin by admin ID
     */
    public Admin findById(int adminId) {
        String sql = "SELECT * FROM admin WHERE admin_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
    	  return new Admin(rs.getInt("admin_id"), rs.getInt("user_id"));
            }                                         
        } catch (SQLException e) {
            System.err.println("Error finding admin by ID: " + e.getMessage());
        }
        return null;
    }
}