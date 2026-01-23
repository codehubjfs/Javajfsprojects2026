package com.appointment.ui;

import com.appointment.model.User;
import com.appointment.service.AuthService;

import java.util.Scanner;

public class LoginUI {
    private Scanner scanner;
    private AuthService authService;

    public LoginUI() {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
    }

    /**
     * Display login screen and handle authentication
     */
    public User showLoginScreen() {
        displayHeader();
        
        while (true) {
            System.out.println("\n1. Login with Mobile Number");
            System.out.println("2. Register as Patient");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    User user = handleLogin();
                    if (user != null) {
                        return user;
                    }
                    break;
                case 2:
                    handleRegistration();
                    break;
                case 3:
                    System.out.println("\n Thank you for using our system!");
                    System.exit(0);
                default:
                    System.out.println(" Invalid choice!");
            }
        }
    }

    /**
     * Handle login process
     */
    private User handleLogin() {
        System.out.print("\nEnter Mobile Number (10 digits): ");
        String mobile = scanner.nextLine().trim();
        
        if (!authService.requestOTP(mobile)) {
            return null;
        }
        
        System.out.print("Enter OTP: ");
        String otp = scanner.nextLine().trim();
        
        return authService.verifyOTPAndLogin(mobile, otp);
    }

    /**
     * Handle registration
     */
    private void handleRegistration() {
        System.out.print("\nEnter Your Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Mobile Number (10 digits): ");
        String mobile = scanner.nextLine().trim();
        
        User user = authService.registerPatient(name, mobile);
        if (user != null) {
            System.out.println(" You can now login with your mobile number!");
        }
    }

    /**
     * Display application header
     */
    private void displayHeader() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  DOCTOR ONLINE APPOINTMENT BOOKING SYSTEM ");
        System.out.println("=".repeat(60));
    }

    /**
     * Get integer input with error handling
     */
    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}