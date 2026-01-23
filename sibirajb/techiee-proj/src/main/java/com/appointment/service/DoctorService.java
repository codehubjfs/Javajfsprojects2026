package com.appointment.service;

import com.appointment.dao.*;
import com.appointment.model.*;
import java.util.List;

public class DoctorService {
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;
    private PrescriptionDAO prescriptionDAO;
    private ReviewDAO reviewDAO;

    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
        this.appointmentDAO = new AppointmentDAO();
        this.prescriptionDAO = new PrescriptionDAO();
        this.reviewDAO = new ReviewDAO();
    }
    /*
     * Get doctor by user ID
     */
    public Doctor getDoctorByUserId(int userId) {
        return doctorDAO.findByUserId(userId);
    }
    /*
     * Get doctor's appointments
     */
    public List<Appointment> getAppointments(int doctorId) {
        return appointmentDAO.findByDoctorId(doctorId);
    }
    /*
     * Update availability
     */
    public boolean updateAvailability(int doctorId, String availability) {
        if (doctorDAO.updateAvailability(doctorId, availability)) {
            System.out.println(" Availability updated to: " + availability);
            return true;
        }
        System.out.println(" Failed to update availability!");
        return false;
    }
    /*
     * Write prescription
     */
    public boolean writePrescription(int appointmentId, int doctorId, 
                                    int patientId, String diagnosis, String notes) {    
        // Check if prescription already exists
        if (prescriptionDAO.findByAppointmentId(appointmentId) != null) {
            System.out.println(" Prescription already exists for this appointment!");
            return false;
        }
        Prescription prescription = new Prescription();
        prescription.setAppointmentId(appointmentId);
        prescription.setDoctorId(doctorId);
        prescription.setPatientId(patientId);
        prescription.setDiagnosis(diagnosis);
        prescription.setNotes(notes);
        int id = prescriptionDAO.createPrescription(prescription);
        if (id > 0) {
            // Update appointment status to COMPLETED
            appointmentDAO.updateStatus(appointmentId, "COMPLETED");
            System.out.println(" Prescription written successfully!");
            return true;
        }
        System.out.println(" Failed to write prescription!");
        return false;
    }
    /*
     * Get reviews for doctor
     */
    public List<Review> getReviews(int doctorId) {
        return reviewDAO.findByDoctorId(doctorId);
    }
}