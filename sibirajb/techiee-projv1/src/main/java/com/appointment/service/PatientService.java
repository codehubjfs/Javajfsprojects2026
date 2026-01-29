package com.appointment.service;

import com.appointment.dao.*;
import com.appointment.model.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class PatientService {
    private PatientDAO patientDAO;
    private SpecializationDAO specializationDAO;
    private DoctorDAO doctorDAO;
    private AppointmentDAO appointmentDAO;
    private PrescriptionDAO prescriptionDAO;
    private PaymentDAO paymentDAO;
    private ReviewDAO reviewDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
        this.specializationDAO = new SpecializationDAO();
        this.doctorDAO = new DoctorDAO();
        this.appointmentDAO = new AppointmentDAO();
        this.prescriptionDAO = new PrescriptionDAO();
        this.paymentDAO = new PaymentDAO();
        this.reviewDAO = new ReviewDAO();
    }
    /**
     * Get or create patient
     */
    public Patient getOrCreatePatient(int userId) {
        Patient patient = patientDAO.findByUserId(userId);
        
        if (patient == null) {
            // Create patient record if doesn't exist
            patient = new Patient();
            patient.setUserId(userId);
            int patientId = patientDAO.createPatient(patient);
            patient.setPatientId(patientId);
        }    
        return patient;
    }
    /**
     * Get all specializations
     */
    public List<Specialization> getAllSpecializations() {
        return specializationDAO.findAll();
    }
    /**
     * Get doctors by specialization
     */
    public List<Doctor> getDoctorsBySpecialization(int specializationId) {
        return doctorDAO.findBySpecialization(specializationId);
    }
    /**
     * Book appointment
     */
    public boolean bookAppointment(int patientId, int doctorId, String date, 
                                  String time, String symptoms) {     
        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setAppointmentDate(Date.valueOf(date));
        appointment.setAppointmentTime(Time.valueOf(time + ":00"));
        appointment.setStatus("SCHEDULED");
        appointment.setSymptoms(symptoms);
        int appointmentId = appointmentDAO.createAppointment(appointment);
        if (appointmentId > 0) {
            System.out.println(" Appointment booked successfully! ID: " + appointmentId);
            return true;
        }
        System.out.println(" Failed to book appointment!");
        return false;
    }

    /**
     * Get patient's appointments
     */
    public List<Appointment> getAppointments(int patientId) {
        return appointmentDAO.findByPatientId(patientId);
    }

    /**
     * Get patient's prescriptions
     */
    public List<Prescription> getPrescriptions(int patientId) {
        return prescriptionDAO.findByPatientId(patientId);
    }

    /**
     * Make payment
     */
    public boolean makePayment(int appointmentId, int patientId, double amount) {
        
        // Check if payment already exists
        if (paymentDAO.findByAppointmentId(appointmentId) != null) {
            System.out.println(" Payment already made for this appointment!");
            return false;
        }

        Payment payment = new Payment();
        payment.setAppointmentId(appointmentId);
        payment.setPatientId(patientId);
        payment.setAmount(amount);
        payment.setPaymentStatus("COMPLETED");

        int paymentId = paymentDAO.createPayment(payment);
        if (paymentId > 0) {
            System.out.println(" Payment of ₹" + amount + " completed successfully!");
            return true;
        }

        System.out.println(" Payment failed!");
        return false;
    }

    /**
     * Give review
     */
    public boolean giveReview(int appointmentId, int patientId, int doctorId, 
                             int rating, String comment) {
        
        // Check if review already exists
        if (reviewDAO.reviewExists(appointmentId)) {
            System.out.println(" Review already given for this appointment!");
            return false;
        }

        Review review = new Review();
        review.setAppointmentId(appointmentId);
        review.setPatientId(patientId);
        review.setDoctorId(doctorId);
        review.setRating(rating);
        review.setComment(comment);

        int reviewId = reviewDAO.createReview(review);
        if (reviewId > 0) {
            System.out.println(" Review submitted successfully!");
            return true;
        }

        System.out.println(" Failed to submit review!");
        return false;
    }
}