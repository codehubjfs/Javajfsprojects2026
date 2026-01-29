package com.appointment.dao;

import com.appointment.model.Appointment;
import com.appointment.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    
    /**
     * Find appointments by doctor and date/time
     */
    public List<Appointment> findByDoctorAndDateTime(int doctorId, Date date, Time time) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, date);
            pstmt.setTime(3, time);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Check if doctor has conflicting appointment
     */
    public boolean hasConflictingAppointment(int doctorId, Date date, Time time) {
        String sql = "SELECT COUNT(*) FROM appointment " +
                    "WHERE doctor_id = ? " +
                    "AND appointment_date = ? " +
                    "AND appointment_time = ? " +
                    "AND status IN ('SCHEDULED', 'CONFIRMED')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, date);
            pstmt.setTime(3, time);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking conflicting appointment: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Find appointments by doctor ID
     */
    public List<Appointment> findByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE doctor_id = ? ORDER BY appointment_date DESC, appointment_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointments by doctor ID: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Find appointments by doctor ID and date
     */
    public List<Appointment> findByDoctorIdAndDate(int doctorId, Date date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE doctor_id = ? AND appointment_date = ? ORDER BY appointment_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, date);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointments by doctor ID and date: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Find today's appointments for a doctor
     */
    public List<Appointment> findTodayAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE doctor_id = ? AND appointment_date = CURDATE() ORDER BY appointment_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding today's appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Find upcoming appointments for a doctor
     */
    public List<Appointment> findUpcomingAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE doctor_id = ? AND appointment_date >= CURDATE() " +
                    "AND status IN ('SCHEDULED', 'CONFIRMED') ORDER BY appointment_date, appointment_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding upcoming appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Create appointment
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
    
    /**
     * Update appointment status
     */
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointment SET status = ? WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, appointmentId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Find appointments by patient ID
     */
    public List<Appointment> findByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE patient_id = ? ORDER BY appointment_date DESC, appointment_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointments by patient ID: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Find all appointments
     */
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment ORDER BY appointment_date DESC, appointment_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                appointments.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all appointments: " + e.getMessage());
        }
        return appointments;
    }
    
    /**
     * Find appointment by ID
     */
    public Appointment findById(int appointmentId) {
        String sql = "SELECT * FROM appointment WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding appointment by ID: " + e.getMessage());
        }
        return null;
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