package controller;

import exception.AuthenticationException;
import model.User;
import service.UserService;
import util.*;

public class AuthController {
    private UserService userService;
    
    public AuthController() {
        this.userService = new UserService();
    }
    
    public User showLoginMenu() {
        while (true) {
        	System.out.println("VEHICLE SERVICE MANAGEMENT - LOGIN");
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            
            int choice = InputUtil.readInt("\nEnter choice: ");
            
            switch (choice) {
                case 1:
                    User user = handleLogin();
                    if (user != null) return user;
                    break;
                case 2:
                    handleRegister();
                    break;
                case 0:
                    System.out.println("\nThank you");
                    System.exit(0);
                default:
                    System.out.println("\nInvalid choice!");
                    InputUtil.pause();
            }
        }
    }
    
    private User handleLogin() {
    	System.out.println("LOGIN");
        
        String email = InputUtil.readString("\nEmail: ");
        String password = InputUtil.readString("Password: ");
        
        try {
            User user = userService.login(email, password);
            System.out.println("\nLogin successful! Welcome, " + user.getFullName());
            return user;
        } 
        catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            InputUtil.pause();
            return null;
        } 
    }
    
    private void handleRegister() {
    	System.out.println("CUSTOMER REGISTRATION");
        
        String fullName = InputUtil.readString("\nFull Name: ");
        String email = InputUtil.readString("Email: ");
        String password = InputUtil.readString("Password: ");
        String phone = InputUtil.readString("Phone (10 digits): ");
        
        try {
            User user = userService.register(fullName, email, password, phone);
            System.out.println("\nRegistration successful!");
            System.out.println("User ID: " + user.getUserId());
            InputUtil.pause();
        } 
        catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            InputUtil.pause();
        }
    }
}