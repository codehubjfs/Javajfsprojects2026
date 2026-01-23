package com.appointment.dao;

import com.appointment.model.Doctor;
import com.appointment.model.User;
import com.appointment.model.Specialization;
import com.appointment.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    /**
     * Find doctor by user ID
     */
    public Doctor findByUserId(int userId) {
        String sql = "SELECT d.*, u.name, u.mobile_number, s.name as spec_name " +
                     "FROM doctor d " +
                     "JOIN user u ON d.user_id = u.user_id " +
                     "JOIN specialization s ON d.specialization_id = s.specialization_id " +
                     "WHERE d.user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding doctor: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all doctors
     */
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT d.*, u.name, u.mobile_number, s.name as spec_name " +
                     "FROM doctor d " +
                     "JOIN user u ON d.user_id = u.user_id " +
                     "JOIN specialization s ON d.specialization_id = s.specialization_id " +
                     "ORDER BY u.name";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                doctors.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctors: " + e.getMessage());
        }
        return doctors;
    }

    /**
     * Find doctors by specialization
     */
    public List<Doctor> findBySpecialization(int specializationId) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT d.*, u.name, u.mobile_number, s.name as spec_name " +
                     "FROM doctor d " +
                     "JOIN user u ON d.user_id = u.user_id " +
                     "JOIN specialization s ON d.specialization_id = s.specialization_id " +
                     "WHERE d.specialization_id = ? AND d.availability = 'AVAILABLE' " +
                     "ORDER BY u.name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, specializationId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                doctors.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctors by specialization: " + e.getMessage());
        }
        return doctors;
    }

    /**
     * Create new doctor
     */
    public int createDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctor (user_id, specialization_id, fees, experience, availability, admin_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, doctor.getUserId());
            pstmt.setInt(2, doctor.getSpecializationId());
            pstmt.setDouble(3, doctor.getFees());
            pstmt.setInt(4, doctor.getExperience());
            pstmt.setString(5, doctor.getAvailability());
            pstmt.setInt(6, doctor.getAdminId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating doctor: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Update doctor availability
     */
    public boolean updateAvailability(int doctorId, String availability) {
        String sql = "UPDATE doctor SET availability = ? WHERE doctor_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, availability);
            pstmt.setInt(2, doctorId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating availability: " + e.getMessage());
        }
        return false;
    }

    private Doctor extractFromResultSet(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setUserId(rs.getInt("user_id"));
        doctor.setSpecializationId(rs.getInt("specialization_id"));
        doctor.setFees(rs.getDouble("fees"));
        doctor.setExperience(rs.getInt("experience"));
        doctor.setAvailability(rs.getString("availability"));
        doctor.setAdminId(rs.getInt("admin_id"));   
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setMobileNumber(rs.getString("mobile_number"));
        doctor.setUser(user);
        
        Specialization spec = new Specialization();
        spec.setSpecializationId(rs.getInt("specialization_id"));
        spec.setName(rs.getString("spec_name"));
        doctor.setSpecialization(spec);
        
        return doctor;
    }
}