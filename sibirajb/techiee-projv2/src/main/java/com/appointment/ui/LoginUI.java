package com.appointment.ui;

import com.appointment.model.User;
import com.appointment.service.AuthService;
import com.appointment.util.InputValidator;

import java.util.Scanner;

public class LoginUI {
    private Scanner scanner;
    private AuthService authService;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public LoginUI() {
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService();
    }

    /**
     * Display login screen and handle authentication
     * @return authenticated User or null
     */
    public User showLoginScreen() {
        displayHeader();

        while (true) {
            System.out.println("\n1. Login with Mobile Number");
            System.out.println("2. Register as Patient");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = getIntInput();

            if (choice == -1) {
                System.out.println("Please enter a valid number!");
                continue;
            }

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
                    System.out.println("\nThank you for using our system!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please enter 1, 2, or 3.");
            }
        }
    }

    /**
     * Handle login process with maximum 3 attempts
     */
    private User handleLogin() {
        int attempts = 0;
        
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("LOGIN (Attempt " + (attempts + 1) + " of " + MAX_LOGIN_ATTEMPTS + ")");
            System.out.println("-".repeat(50));
            
            // Get mobile number with validation
            String mobile = "";
            boolean validMobile = false;
            
            while (!validMobile) {
                System.out.print("\nEnter Mobile Number (10 digits starting with 6-9): ");
                mobile = scanner.nextLine().trim();
                
                if (InputValidator.isValidMobileNumber(mobile)) {
                    validMobile = true;
                } else {
                    System.out.println("\n" + InputValidator.getMobileValidationMessage(mobile));
                    System.out.println("Please try again.");
                }
            }
            
            // Request OTP
            if (!authService.requestOTP(mobile)) {
                attempts++;
                System.out.println("Failed to send OTP. Please try again.");
                if (attempts >= MAX_LOGIN_ATTEMPTS) {
                    break;
                }
                System.out.println("Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - attempts));
                continue;
            }
            
            // Get OTP with attempt handling
            int otpAttempts = 0;
            
            while (otpAttempts < MAX_LOGIN_ATTEMPTS) {
                System.out.print("Enter OTP (6 digits): ");
                String otp = scanner.nextLine().trim();
                
                // Validate OTP format
                if (!InputValidator.isValidOTP(otp)) {
                    otpAttempts++;
                    System.out.println("Invalid OTP format! Must be 6 digits.");
                    System.out.println("OTP Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - otpAttempts));
                    if (otpAttempts >= MAX_LOGIN_ATTEMPTS) {
                        System.out.println("Maximum OTP attempts reached.");
                        break;
                    }
                    continue;
                }
                
                // Verify OTP
                User user = authService.verifyOTPAndLogin(mobile, otp);
                if (user != null) {
//                    System.out.println("\nLogin successful! Welcome, " + user.getName() + "!");
                    return user;
                } else {
                    otpAttempts++;
                    System.out.println("Incorrect OTP!");
                    System.out.println("OTP Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - otpAttempts));
                    if (otpAttempts >= MAX_LOGIN_ATTEMPTS) {
                        System.out.println("Maximum OTP attempts reached.");
                        break;
                    }
                }
            }
            
            // If OTP attempts exhausted for this mobile number
            attempts++;
            if (attempts < MAX_LOGIN_ATTEMPTS) {
                System.out.println("\nLogin attempts remaining: " + (MAX_LOGIN_ATTEMPTS - attempts));
                System.out.print("Would you like to try again with a different mobile number? (y/n): ");
                String choice = scanner.nextLine().trim().toLowerCase();
                if (!choice.equals("y")) {
                    System.out.println("Login cancelled.");
                    return null;
                }
            }
        }
        
        // All attempts exhausted
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MAXIMUM ATTEMPTS REACHED!");
        System.out.println("Please try again later or contact support.");
        System.out.println("=".repeat(60));
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
        return null;
    }

    /**
     * Handle patient registration - no attempt limit
     */
    private void handleRegistration() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PATIENT REGISTRATION");
        System.out.println("=".repeat(60));
        
        // Get name
        System.out.print("\nEnter Your Name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        
        // Get mobile number with validation
        String mobile = "";
        boolean validMobile = false;
        
        while (!validMobile) {
            System.out.print("Enter Mobile Number (10 digits starting with 6-9): ");
            mobile = scanner.nextLine().trim();
            
            if (InputValidator.isValidMobileNumber(mobile)) {
                validMobile = true;
            } else {
                System.out.println("\n" + InputValidator.getMobileValidationMessage(mobile));
                System.out.println("Please try again.");
            }
        }
        
        // Confirm registration
        System.out.println("\nRegistration Details:");
        System.out.println("Name: " + name);
        System.out.println("Mobile: " + mobile);
        System.out.print("\nConfirm registration? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("y")) {
            System.out.println("Registration cancelled.");
            return;
        }
        
        // Attempt registration
        User user = authService.registerPatient(name, mobile);
        if (user != null) {
            System.out.println("\nRegistration successful!");
            System.out.println("You can now login with your mobile number.");
        } else {
            System.out.println("Registration failed! Mobile number may already be registered.");
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