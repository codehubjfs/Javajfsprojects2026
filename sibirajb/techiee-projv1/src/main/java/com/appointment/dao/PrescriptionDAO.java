package com.appointment.dao;

import com.appointment.model.Prescription;
import com.appointment.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    /**
     * Create prescription
     */
    public int createPrescription(Prescription prescription) {
        String sql = "INSERT INTO prescription (appointment_id, doctor_id, patient_id, diagnosis, notes) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, prescription.getAppointmentId());
            pstmt.setInt(2, prescription.getDoctorId());
            pstmt.setInt(3, prescription.getPatientId());
            pstmt.setString(4, prescription.getDiagnosis());
            pstmt.setString(5, prescription.getNotes());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating prescription: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Get prescriptions by patient ID
     */
    public List<Prescription> findByPatientId(int patientId) {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT p.*, u.name as doctor_name " +
                     "FROM prescription p " +
                     "JOIN doctor d ON p.doctor_id = d.doctor_id " +
                     "JOIN user u ON d.user_id = u.user_id " +
                     "WHERE p.patient_id = ? ORDER BY p.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                prescriptions.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching prescriptions: " + e.getMessage());
        }
        return prescriptions;
    }

    /**
     * Find prescription by appointment ID
     */
    public Prescription findByAppointmentId(int appointmentId) {
        String sql = "SELECT * FROM prescription WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding prescription: " + e.getMessage());
        }
        return null;
    }

    private Prescription extractFromResultSet(ResultSet rs) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(rs.getInt("prescription_id"));
        prescription.setAppointmentId(rs.getInt("appointment_id"));
        prescription.setDoctorId(rs.getInt("doctor_id"));
        prescription.setPatientId(rs.getInt("patient_id"));
        prescription.setDiagnosis(rs.getString("diagnosis"));
        prescription.setNotes(rs.getString("notes"));
        return prescription;
    }
}