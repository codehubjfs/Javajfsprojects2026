package com.appointment.service;

import com.appointment.dao.*;
import com.appointment.model.*;
import java.util.List;
import java.util.ArrayList;

public class DoctorService {
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;
    private PrescriptionDAO prescriptionDAO;
    private ReviewDAO reviewDAO;
    
    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
        this.appointmentDAO = new AppointmentDAO();
        this.patientDAO = new PatientDAO();
        this.prescriptionDAO = new PrescriptionDAO();
        this.reviewDAO = new ReviewDAO();
    }
    
    /**
     * Get doctor by user ID
     */
    public Doctor getDoctorByUserId(int userId) {
        return doctorDAO.findByUserId(userId);
    }
    /**
     * Get appointment by ID
     */
    public Appointment getAppointmentById(int appointmentId) {
        return appointmentDAO.findById(appointmentId);
    }
    /**
     * Get doctor's appointments
     */
    public List<Appointment> getAppointments(int doctorId) {
        List<Appointment> appointments = appointmentDAO.findByDoctorId(doctorId);
        return appointments != null ? appointments : new ArrayList<>();
    }
    
    /**
     * Get today's appointments
     */
    public List<Appointment> getTodayAppointments(int doctorId) {
        List<Appointment> appointments = appointmentDAO.findTodayAppointmentsByDoctorId(doctorId);
        return appointments != null ? appointments : new ArrayList<>();
    }
    
    /**
     * Get upcoming appointments
     */
    public List<Appointment> getUpcomingAppointments(int doctorId) {
        List<Appointment> appointments = appointmentDAO.findUpcomingAppointmentsByDoctorId(doctorId);
        return appointments != null ? appointments : new ArrayList<>();
    }
    
    /**
     * Update appointment status
     */
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        return appointmentDAO.updateAppointmentStatus(appointmentId, status);
    }
    
    /**
     * Update doctor availability
     */
    public boolean updateAvailability(int doctorId, String availability) {
        return doctorDAO.updateAvailability(doctorId, availability);
    }
    
    /**
     * Get patient details
     */
    public Patient getPatientById(int patientId) {
        return patientDAO.findById(patientId);
    }
    
    /**
     * Create prescription
     */
    public boolean createPrescription(int appointmentId, String diagnosis, String notes) {
        // Get appointment to get patient ID
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            System.out.println(" Appointment not found!");
            return false;
        }
        
        Prescription prescription = new Prescription();
        prescription.setAppointmentId(appointmentId);
        prescription.setPatientId(appointment.getPatientId());
        prescription.setDoctorId(appointment.getDoctorId());
        prescription.setDiagnosis(diagnosis);
        prescription.setNotes(notes);
        
        int prescriptionId = prescriptionDAO.createPrescription(prescription);
        return prescriptionId > 0;
    }
    /**
     * Get doctor's prescriptions
     */
    public List<Prescription> getPrescriptions(int doctorId) {
        List<Prescription> prescriptions = prescriptionDAO.findByDoctorId(doctorId);
        return prescriptions != null ? prescriptions : new ArrayList<>();
    }
    
    /**
     * Get doctor's reviews
     */
    public List<Review> getReviews(int doctorId) {
        List<Review> reviews = reviewDAO.findByDoctorId(doctorId);
        return reviews != null ? reviews : new ArrayList<>();
    }
    
    /**
     * Write prescription (legacy method for compatibility)
     */
    public boolean writePrescription(int appointmentId, int doctorId, int patientId, 
                                   String diagnosis, String notes) {
        return createPrescription(appointmentId, diagnosis, notes);
    }
}