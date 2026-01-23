package com.appointment.dao;

import com.appointment.model.Specialization;
import com.appointment.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecializationDAO {
    /*
     * Get all specializations
     */
    public List<Specialization> findAll() {
        List<Specialization> specializations = new ArrayList<>();
        String sql = "SELECT * FROM specialization ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                specializations.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching specializations: " + e.getMessage());
        }
        return specializations;
    }
    /*
     * Find specialization by ID
     */
    public Specialization findById(int specializationId) {
        String sql = "SELECT * FROM specialization WHERE specialization_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, specializationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding specialization: " + e.getMessage());
        }
        return null;
    }

    /**
     * Create new specialization
     */
    public int createSpecialization(Specialization specialization) {
        String sql = "INSERT INTO specialization (name, description) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, specialization.getName());
            pstmt.setString(2, specialization.getDescription());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating specialization: " + e.getMessage());
        }
        return -1;
    }
    private Specialization extractFromResultSet(ResultSet rs) throws SQLException {
        Specialization spec = new Specialization();
        spec.setSpecializationId(rs.getInt("specialization_id"));
        spec.setName(rs.getString("name"));
        spec.setDescription(rs.getString("description"));
        return spec;
    }
}