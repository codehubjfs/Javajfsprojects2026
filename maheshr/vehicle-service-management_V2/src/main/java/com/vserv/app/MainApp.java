package com.vserv.app;

import com.vserv.controller.AdminController;
import com.vserv.controller.AdvisorController;
import com.vserv.controller.AuthController;
import com.vserv.controller.CustomerController;
import com.vserv.controller.GuestController;
import com.vserv.model.User;
import com.vserv.exception.*;

/**
 * Main application entry point
 * 
 * @author Mahesh R
 */
public class MainApp {
    public static void main(String[] args) {
        AuthController authController = new AuthController();
        
        // Login/Register/Guest
        User user = authController.showLoginMenu();
        
        // Route based on role
        try {
        if (user != null) {
            switch (user.getRoleName()) {
                case "ADMIN":
                    AdminController adminController = new AdminController(user);
                    adminController.showDashboard();
                    break;
                    
                case "CUSTOMER":
                    CustomerController customerController = new CustomerController(user);
                    customerController.showDashboard();
                    break;
                    
                case "ADVISOR":
                    AdvisorController advisorController = new AdvisorController(user);
                    advisorController.showDashboard();
                    break;
                    
                case "GUEST":
                    GuestController guestController = new GuestController();
                    guestController.showDashboard();
                    break;
                    
                default:
                    System.out.println("\nUnknown role: " + user.getRoleName());
            }
        }
        }
        catch (AuthorizationException e) {
            System.out.println("\nAuthorization Error: " + e.getMessage());
        }
    }
}