package com.vserv.controller;

import com.vserv.exception.AuthenticationException;
import com.vserv.model.User;
import com.vserv.service.UserService;
import com.vserv.util.*;

/**
 * Handles authentication
 * 
 * @author Mahesh R
 */
public class AuthController {
    private UserService _userService;
    private static final int MAX_RETRIES = 3;
    
    public AuthController() {
        this._userService = new UserService();
    }
    
    /**
     * Display menu and role access
     * 
     * @return authenticated user object or guest user
     */
    public User showLoginMenu() {
        while (true) {
            System.out.println("""
                    
                    VEHICLE SERVICE MANAGEMENT
                    
                    1. Login
                    2. Register as Customer
                    3. Guest Login
                    0. Exit
                    """);
            
            int choice = InputValidator.readInt("Enter choice: ");
            
            switch (choice) {
                case 1:
                    User user = handleLogin();
                    if (user != null) return user;
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    return createGuestUser();
                case 0:
                    System.out.println("\nThank you.");
                    System.exit(0);
                default:
                    System.out.println("\nInvalid choice. Please select 1-3 or 0 to exit.");
                    InputValidator.load();
            }
        }
    }
    
    /**
     * Create a temporary guest user object for browsing
     */
    private User createGuestUser() {
        User guest = new User();
        guest.setUserId(0);
        guest.setFullName("Guest User");
        guest.setEmail("guest@system.local");
        guest.setRoleName("GUEST");
        guest.setStatus("ACTIVE");
        return guest;
    }
    
    private User handleLogin() {
        System.out.println("\nLOGIN");
        
        String email = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            email = InputValidator.readString("\nEmail: ");
            if (FieldValidator.isValidEmail(email)) {
                break;
            }
            System.out.println("Invalid email format. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
            if (i == MAX_RETRIES - 1) {
                System.out.println("\nToo many invalid attempts. Returning to menu.");
                InputValidator.load();
                return null;
            }
        }
        
        String password = InputValidator.readString("Password: ");
        
        try {
            User user = _userService.login(email, password);
            System.out.println("\nLogin successful. Welcome, " + user.getFullName());
            return user;
        } 
        catch (AuthenticationException e) {
            System.out.println("\nError: " + e.getMessage());
            InputValidator.load();
            return null;
        } 
    }
    
    private void handleRegister() {
        System.out.println("\nCUSTOMER REGISTRATION");
        
        String fullName = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            fullName = InputValidator.readString("\nFull Name: ");
            if (FieldValidator.isValidFullName(fullName)) {
                break;
            }
            System.out.println("Invalid name. Use only letters, spaces, hyphens (2-100 chars). " + (MAX_RETRIES - i - 1) + " attempts remaining.");
            if (i == MAX_RETRIES - 1) {
                System.out.println("\nToo many invalid attempts. Returning to menu.");
                InputValidator.load();
                return;
            }
        }
        
        String email = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            email = InputValidator.readString("Email: ");
            if (FieldValidator.isValidEmail(email)) {
                break;
            }
            System.out.println("Invalid email format (e.g. user123@domain.com). " + (MAX_RETRIES - i - 1) + " attempts remaining.");
            if (i == MAX_RETRIES - 1) {
                System.out.println("\nToo many invalid attempts. Returning to menu.");
                InputValidator.load();
                return;
            }
        }
        
        String password = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            password = InputValidator.readString("Password: ");
            if (FieldValidator.isValidPassword(password)) {
                break;
            }
            System.out.println("Password must be at least 8 characters with uppercase, lowercase, digit, and special character.");
            System.out.println((MAX_RETRIES - i - 1) + " attempts remaining.");
            if (i == MAX_RETRIES - 1) {
                System.out.println("\nToo many invalid attempts. Returning to menu.");
                InputValidator.load();
                return;
            }
        }
        
        String phone = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            phone = InputValidator.readString("Phone (10 digits): ");
            if (FieldValidator.isValidPhone(phone)) {
                break;
            }
            System.out.println("Invalid phone number. Must be 10 digits. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
            if (i == MAX_RETRIES - 1) {
                System.out.println("\nToo many invalid attempts. Returning to menu.");
                InputValidator.load();
                return;
            }
        }
        
        String gender = null;
        for (int i = 0; i < MAX_RETRIES; i++) {
            System.out.println("\nGender:");
            System.out.println("1. MALE");
            System.out.println("2. FEMALE");
            System.out.println("3. PREFER NOT TO SAY");
            
            int genderChoice = InputValidator.readInt("Select gender (1-3): ");
            
            gender = switch (genderChoice) {
                case 1 -> "MALE";
                case 2 -> "FEMALE";
                case 3 -> "PREFER_NOT_TO_SAY";
                default -> null;
            };
            
            if (gender != null) {
                break;
            }
            System.out.println("Invalid choice. " + (MAX_RETRIES - i - 1) + " attempts remaining.");
            if (i == MAX_RETRIES - 1) {
                System.out.println("\nToo many invalid attempts. Returning to menu.");
                InputValidator.load();
                return;
            }
        }
        
        try {
            User user = _userService.register(fullName, email, password, phone, gender);
            System.out.println("\nRegistration successful");
            System.out.println("User ID: " + user.getUserId());
            System.out.println("\nYou can now login with your credentials.");
            InputValidator.load();
        } 
        catch (AuthenticationException e) {
            System.out.println("\nError: " + e.getMessage());
            InputValidator.load();
        }
    }
}