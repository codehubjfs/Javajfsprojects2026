package com.appointment.dao;

import com.appointment.model.Payment;
import com.appointment.util.DBConnection;

import java.sql.*;

public class PaymentDAO {

    /**
     * Create payment
     */
    public int createPayment(Payment payment) {
        String sql = "INSERT INTO payment (appointment_id, patient_id, amount, payment_status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payment.getAppointmentId());
            pstmt.setInt(2, payment.getPatientId());
            pstmt.setDouble(3, payment.getAmount());
            pstmt.setString(4, payment.getPaymentStatus());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Find payment by appointment ID
     */
    public Payment findByAppointmentId(int appointmentId) {
        String sql = "SELECT * FROM payment WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setAppointmentId(rs.getInt("appointment_id"));
                payment.setPatientId(rs.getInt("patient_id"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setPaymentStatus(rs.getString("payment_status"));
                return payment;
            }
        } catch (SQLException e) {
            System.err.println("Error finding payment: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update payment status
     */
    public boolean updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE payment SET payment_status = ? WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, paymentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
        }
        return false;
    }
}
