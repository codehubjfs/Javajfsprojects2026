package com.appointment.service;

import com.appointment.dao.*;
import com.appointment.model.*;

import java.util.List;

public class AdminService {
    private DoctorDAO doctorDAO;
    private UserDAO userDAO;
    private SpecializationDAO specializationDAO;
    private AppointmentDAO appointmentDAO;
    private AdminDAO adminDAO;
    
    public AdminService() {
        this.doctorDAO = new DoctorDAO();
        this.userDAO = new UserDAO();
        this.specializationDAO = new SpecializationDAO();
        this.appointmentDAO = new AppointmentDAO();
        this.adminDAO = new AdminDAO();
    }
    /**
     * Add new doctor
     */
    public boolean addDoctor(String name, String mobile, int specializationId, 
                            double fees, int experience, int adminUserId) {
        // Create user for doctor
        User user = new User();
        user.setName(name);
        user.setMobileNumber(mobile);
        user.setRole("DOCTOR");
        
        int userId = userDAO.createUser(user);
        if (userId <= 0) {
            System.out.println(" Failed to create user!");
            return false;
        }
        // Get admin ID from user ID
        Admin admin = adminDAO.findByUserId(adminUserId);
        if (admin == null) {
            System.out.println(" Admin not found!");
            return false;
        }
        // Create doctor 
        Doctor doctor = new Doctor();
        doctor.setUserId(userId);
        doctor.setSpecializationId(specializationId);
        doctor.setFees(fees);
        doctor.setExperience(experience);
        doctor.setAvailability("AVAILABLE");
        doctor.setAdminId(admin.getAdminId());
        int doctorId = doctorDAO.createDoctor(doctor);
        if (doctorId > 0) {
            System.out.println(" Doctor added successfully!");
            return true;
        }
        System.out.println(" Failed to add doctor!");
        return false;
    }
    /*
     * Get all doctors 
     */
    public List<Doctor> getAllDoctors() {
        return doctorDAO.findAll();
    }
    /*
     *  Add new specialization 
     */
    public boolean addSpecialization(String name, String description) {
        Specialization spec = new Specialization();
        spec.setName(name);
        spec.setDescription(description);
        int id = specializationDAO.createSpecialization(spec);
        if (id > 0) {
            System.out.println(" Specialization added successfully!");
            return true;
        }

        System.out.println(" Failed to add specialization!");
        return false;
    }
    /*
     * Get all appointments
     */
    public List<Appointment> getAllAppointments() {
        return appointmentDAO.findAll();
    }

    /*
     * Get all specializations 
     */
    public List<Specialization> getAllSpecializations() {
        return specializationDAO.findAll();
    }
}