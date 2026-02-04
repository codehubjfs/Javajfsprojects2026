package com.recharge.menu;

import com.recharge.exception.EmailFormatException;
import com.recharge.exception.PasswordFormatException;
import com.recharge.exception.UserNameFormatException;
import com.recharge.service.AdminReadService;
import com.recharge.service.UserRegistrationService;
import com.recharge.util.InputUtil;
import com.recharge.util.ValidationUtil;

public class GuestMenu {

    private final UserRegistrationService registrationService = new UserRegistrationService();

    private final AdminReadService readService = new AdminReadService();

    public void start() {

        boolean running = true;

        while (running) {
            System.out.println("""
                    
                === GUEST MENU ===
                1. Browse Recharge Plans
                2. Register
                3. Back to Main Menu
                """);

            int choice = InputUtil.readInt("Choose option:", 1, 3);

            try {
                switch (choice) {

                    case 1 -> browsePlans();

                    case 2 -> register();

                    case 3 -> running = false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Guest Read only browse plans
     */
    private void browsePlans() {
        System.out.println("\n=== AVAILABLE RECHARGE PLANS ===");
        readService.viewRechargePlans();   // already filters active plans
        System.out.println("\n(Login required to recharge)");
    }

    /**
     * registration
     * @throws EmailFormatException 
     */
    private void register() throws EmailFormatException {

        System.out.println("\n=== USER REGISTRATION ===");

        String name;
        while(true) {
        	name = InputUtil.readString("Full Name: ");
        	try {
        		ValidationUtil.isValidUserName(name);
        		break;
        	}
        	catch(UserNameFormatException e) {
        		System.out.println(e.getMessage());
        	}
        }

        String gender;
        do {
            gender = InputUtil.readString("Gender (MALE/FEMALE/OTHER): ").toUpperCase();
            if (!ValidationUtil.isValidGender(gender)) {
                System.out.println("Invalid gender. Allowed: MALE, FEMALE, OTHER");
            }
        } while (!ValidationUtil.isValidGender(gender));

        String email;
        while(true) {
        	email = InputUtil.readString("Email: ");
        	try {
        		ValidationUtil.isValidEmail(email);
        		break;
        	}
        	catch(EmailFormatException e) {
        		System.out.println(e.getMessage());
        	}
        }

        String password; 
        while(true) {
        	password = InputUtil.readString("Password: ");
        	try {
        		ValidationUtil.isValidPassword(password);
        		break;
        	}
        	catch(PasswordFormatException e) {
        		System.out.println(e.getMessage());
        	}
        }

        registrationService.register(name, gender, email, password);

        System.out.println("""
                
            Registration successful.
            Please login as USER to continue.
            """);
    }
}
