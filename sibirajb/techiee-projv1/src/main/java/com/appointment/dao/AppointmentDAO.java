
package com.appointment.dao;
import com.appointment.model.Appointment;
import com.appointment.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AppointmentDAO {
    /**
     * Create new appointment
     */
    public int createAppointment(Appointment appointment) {
        String sql = "INSERT INTO appointment (patient_id, doctor_id, appointment_date, appointment_time, status, symptoms) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";  
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setDate(3, appointment.getAppointmentDate());
            pstmt.setTime(4, appointment.getAppointmentTime());
            pstmt.setString(5, appointment.getStatus());
            pstmt.setString(6, appointment.getSymptoms());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating appointment: " + e.getMessage());
        }
        return -1;
    }
    /*
     * Get appointments by patient ID
     */
    public List<Appointment> findByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, u.name as doctor_name, s.name as spec_name " +
                     "FROM appointment a " +
                     "JOIN doctor d ON a.doctor_id = d.doctor_id " +
                     "JOIN user u ON d.user_id = u.user_id " +
                     "JOIN specialization s ON d.specialization_id = s.specialization_id " +
                     "WHERE a.patient_id = ? ORDER BY a.appointment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patient appointments: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Get appointments by doctor ID
     */
    public List<Appointment> findByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, u.name as patient_name " +
                     "FROM appointment a " +
                     "JOIN patient p ON a.patient_id = p.patient_id " +
                     "JOIN user u ON p.user_id = u.user_id " +
                     "WHERE a.doctor_id = ? ORDER BY a.appointment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching doctor appointments: " + e.getMessage());
        }
        return appointments;
    }

    /**
     * Get all appointments (for admin)
     */
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, " +
                     "u1.name as patient_name, u2.name as doctor_name " +
                     "FROM appointment a " +
                     "JOIN patient p ON a.patient_id = p.patient_id " +
                     "JOIN user u1 ON p.user_id = u1.user_id " +
                     "JOIN doctor d ON a.doctor_id = d.doctor_id " +
                     "JOIN user u2 ON d.user_id = u2.user_id " +
                     "ORDER BY a.appointment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
                  	
  while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all appointments: " + e.getMessage());
        }
        return appointments;
    }
    /*
     * Update appointment status
     */
    public boolean updateStatus(int appointmentId, String status) {
        String sql = "UPDATE appointment SET status = ? WHERE appointment_id = ?";        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {           
            pstmt.setString(1, status);
            pstmt.setInt(2, appointmentId);           
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }
        return false;
    }
    private Appointment extractFromResultSet(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setAppointmentDate(rs.getDate("appointment_date"));
        appointment.setAppointmentTime(rs.getTime("appointment_time"));
        appointment.setStatus(rs.getString("status"));
        appointment.setSymptoms(rs.getString("symptoms"));
        return appointment;
    }
}
