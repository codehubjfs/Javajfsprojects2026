package com.recharge;

import com.recharge.config.DBConnection;
import com.recharge.exception.AuthenticationException;
//import com.recharge.exception.EmailFormatException;
//import com.recharge.exception.PasswordFormatException;
import com.recharge.menu.AdminMenu;
import com.recharge.menu.GuestMenu;
import com.recharge.menu.UserMenu;
import com.recharge.model.Role;
import com.recharge.model.User;
import com.recharge.service.AuthService;
import com.recharge.util.InputUtil;
//import com.recharge.util.ValidationUtil;

public class Main {
    public static void main(String[] args) {
        
        //initializing db connection
        DBConnection.initialize();

        //creating object to authorize the user credentials
        AuthService authService = new AuthService();
        
        boolean running = true;
        
        try {
            while(running) {

                System.out.println("""
                        ==== MOBILE RECHARGE SYSTEM ====
                        1. Admin Login
                        2. User Login
                        3. Guest 
                        4. Exit
                        """);
                
   
                int choice = InputUtil.readInt("Choose option:", 1, 4);
                
                switch(choice) {
                    case 1 -> handleLogin(authService, Role.ADMIN);
     
                    case 2 -> handleLogin(authService, Role.USER);
                       
                    case 3 -> new GuestMenu().start();

                    case 4 ->{
                    	running = false;
                    	System.out.println("Exiting application");                    	
                    }

                    default -> System.out.println("Invalid choice");

                }
            }
        }
        catch(Exception e) {
        	System.out.println(e.getMessage());
        }
        finally {
            DBConnection.close();
        }
    }
       
    private static void handleLogin(AuthService authService, Role expectedRole) {
        String email = InputUtil.readString("Email: ");
        String password = InputUtil.readString("Password: ");

        try {
            User user = authService.login(email, password);

            if (user.getRole() == expectedRole) {
            	
                switch (expectedRole) {
                    case ADMIN -> new AdminMenu(user).start();
                    case USER  -> new UserMenu(user).start();
                }
            } 
            else {
                System.out.println("Invalid role for this login option");
            }

        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }

//    private static String readValidEmail() {
//        while (true) {
//            String email = InputUtil.readString("Email: ");
//            try {
//                ValidationUtil.isValidEmail(email);
//                return email;
//            } catch (EmailFormatException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }

//    private static String readValidPassword() {
//        while (true) {
//            String password = InputUtil.readString("Password: ");
//            try {
//                ValidationUtil.isValidPassword(password);
//                return password;
//            } catch (PasswordFormatException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
}
