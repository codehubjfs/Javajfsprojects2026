
package com.appointment.service;

import com.appointment.dao.UserDAO;
import com.appointment.model.User;
import com.appointment.util.OTPGenerator;
import com.appointment.util.InputValidator;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Request OTP for login
     */
    public boolean requestOTP(String mobileNumber) {
        if (!InputValidator.isValidMobileNumber(mobileNumber)) {
            System.out.println(" Invalid mobile number format!");
            return false;
        }

        User user = userDAO.findByMobileNumber(mobileNumber);
        
        if (user == null) {
            System.out.println(" Mobile number not registered!");
            return false;
        }

        String otp = OTPGenerator.generateOTP(mobileNumber);
        OTPGenerator.displayOTP(mobileNumber, otp);
        
        return true;
    }

    /**
     * Verify OTP and login
     */
    public User verifyOTPAndLogin(String mobileNumber, String otp) {
        if (!InputValidator.isValidOTP(otp)) {
            System.out.println(" Invalid OTP format!");
            return null;
        }

        if (OTPGenerator.verifyOTP(mobileNumber, otp)) {
            User user = userDAO.findByMobileNumber(mobileNumber);
            System.out.println(" Login successful! Welcome " + user.getName());
            return user;
        } else {
            System.out.println(" Invalid OTP!");
            return null;
        }
    }

    /**
     * Register new user (patient)
     */
    public User registerPatient(String name, String mobileNumber) {
        if (!InputValidator.isValidMobileNumber(mobileNumber)) {
            System.out.println(" Invalid mobile number!");
            return null;
        }

        if (userDAO.findByMobileNumber(mobileNumber) != null) {
            System.out.println(" Mobile number already registered!");
            return null;
        }

        User user = new User();
        user.setName(name);
        user.setMobileNumber(mobileNumber);
        user.setRole("PATIENT");

        int userId = userDAO.createUser(user);
        if (userId > 0) {
            user.setUserId(userId);
            System.out.println(" Registration successful!");
            return user;
        }

        System.out.println(" Registration failed!");
        return null;
    }
}