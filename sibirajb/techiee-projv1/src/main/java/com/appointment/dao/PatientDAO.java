package com.appointment.dao;

import com.appointment.model.Patient;
import com.appointment.model.User;
import com.appointment.util.DBConnection;

import java.sql.*;

public class PatientDAO {

    /**
     * Find patient by user ID
     */
    public Patient findByUserId(int userId) {
        String sql = "SELECT p.*, u.name, u.mobile_number " +
                     "FROM patient p " +
                     "JOIN user u ON p.user_id = u.user_id " +
                     "WHERE p.user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding patient: " + e.getMessage());
        }
        return null;
    }

    /**
     * Create new patient
     */
    public int createPatient(Patient patient) {
        String sql = "INSERT INTO patient (user_id, age, gender, phone, address) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, patient.getUserId());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getGender());
            pstmt.setString(4, patient.getPhone());
            pstmt.setString(5, patient.getAddress());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating patient: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Find patient by ID
     */
    public Patient findById(int patientId) {
        String sql = "SELECT p.*, u.name, u.mobile_number " +
                     "FROM patient p " +
                     "JOIN user u ON p.user_id = u.user_id " +
                     "WHERE p.patient_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding patient by ID: " + e.getMessage());
        }
        return null;
    }

    private Patient extractFromResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setUserId(rs.getInt("user_id"));
        patient.setAge(rs.getInt("age"));
        patient.setGender(rs.getString("gender"));
        patient.setPhone(rs.getString("phone"));
        patient.setAddress(rs.getString("address"));
        
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setMobileNumber(rs.getString("mobile_number"));
        patient.setUser(user);
        
        return patient;
    }
}