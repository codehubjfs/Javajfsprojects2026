package com.appointment.dao;

import com.appointment.model.Review;
import com.appointment.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    /**
     * Create review
     */
    public int createReview(Review review) {
        String sql = "INSERT INTO review (appointment_id, patient_id, doctor_id, rating, comment) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, review.getAppointmentId());
            pstmt.setInt(2, review.getPatientId());
            pstmt.setInt(3, review.getDoctorId());
            pstmt.setInt(4, review.getRating());
            pstmt.setString(5, review.getComment());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating review: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Get reviews for doctor
     */
    public List<Review> findByDoctorId(int doctorId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.name as patient_name " +
                     "FROM review r " +
                     "JOIN patient p ON r.patient_id = p.patient_id " +
                     "JOIN user u ON p.user_id = u.user_id " +
                     "WHERE r.doctor_id = ? ORDER BY r.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews: " + e.getMessage());
        }
        return reviews;
    }

    /**
     * Check if review exists for appointment
     */
    public boolean reviewExists(int appointmentId) {
        String sql = "SELECT COUNT(*) FROM review WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking review: " + e.getMessage());
        }
        return false;
    }

    private Review extractFromResultSet(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setAppointmentId(rs.getInt("appointment_id"));
        review.setPatientId(rs.getInt("patient_id"));
        review.setDoctorId(rs.getInt("doctor_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        return review;
    }
}