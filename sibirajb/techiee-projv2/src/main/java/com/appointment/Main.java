
package com.appointment;

import com.appointment.model.User;
import com.appointment.ui.LoginUI;
import com.appointment.ui.MenuFactory;
import com.appointment.util.DBConnection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Doctor Online Appointment Booking System...");
        // Test database connection
        if (!DBConnection.testConnection()) {
            System.err.println(" Failed to connect to database!");
            System.err.println("Please check your MySQL configuration in DBConnection.java");
            System.err.println("Make sure MySQL is running and the database 'doctor_appointment_system' exists.");
            return;
        }  
        try {
            // Show login screen
            LoginUI loginUI = new LoginUI();
            User loggedInUser = loginUI.showLoginScreen();
            
            if (loggedInUser != null) {
                // Show role-based menu
                MenuFactory.showRoleBasedMenu(loggedInUser);
            }
        } catch (Exception e) {
            System.err.println(" An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
            System.out.println("\n Application closed successfully!");
        }
    }
}