package service;

import dao.*;
import model.Location;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Scanner;

public class AuthService {

    public static void registerOrLogin(Scanner scan) {

        while (true) {
            System.out.println("\n--- Authentication Menu ---");
            System.out.println("1. Login");
            System.out.println("2. Register as Guest");
            System.out.println("3. Admin Register Employee");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = scan.nextInt();
            scan.nextLine();

            switch (choice) {
                case 1:
                	login(scan);
                	break;
                case 2:
                	registerGuest(scan);
                	break;
                case 3:
                	registerEmployee(scan);
                	break;
                case 4 :
                    System.out.println("Exiting...");
                    return;
                default:
                	System.out.println("Invalid choice!");
            }
        }
    }

    // ------------------------- GUEST REGISTRATION -------------------------
    public static void registerGuest(Scanner scan) {
        try {
            System.out.println("\n--- GUEST REGISTRATION ---");

            System.out.print("Name: ");
            String name = scan.nextLine();

            System.out.print("Email: ");
            String email = scan.nextLine();

            System.out.print("Phone: ");
            String phone = scan.nextLine();

            System.out.print("Gender (Male/Female/Prefer not to say): ");
            String gender = scan.nextLine();

            System.out.print("Password: ");
            String password = scan.nextLine();

            // ------------------------- LOCATION INFO -------------------------
            System.out.println("\n--- LOCATION DETAILS ---");
            System.out.print("Address: ");
            String address = scan.nextLine();
            System.out.print("City: ");
            String city = scan.nextLine();
            System.out.print("State: ");
            String state = scan.nextLine();
            System.out.print("Country: ");
            String country = scan.nextLine();
            System.out.print("Zipcode: ");
            String zipcode = scan.nextLine();
            System.out.print("Nationality: ");
            String nationality = scan.nextLine();

            Location location = new Location(address, city, state, country, zipcode, nationality, LocalDateTime.now().toString());

            LocationDAO locationDAO = new LocationDAO();
            locationDAO.addLocation(location);

            int locationId = locationDAO.getLastInsertedId();

            // ------------------------- USER CREATION -------------------------
            UserDAO userDAO = new UserDAO();
            int userId = userDAO.createUser(name, email, phone, gender, locationId);

            // ------------------------- GUEST CREATION -------------------------
            GuestDAO guestDAO = new GuestDAO();
            guestDAO.addGuest(userId, "GUEST-" + userId);

            // ------------------------- LOGIN ENTRY -------------------------
            LoginDAO loginDAO = new LoginDAO();
            loginDAO.registerLogin(userId, password, "Guest");

            System.out.println("Your user id is: " + userId);
            System.out.println("Guest registered successfully! Please login.");

        } catch (Exception e) {
            System.out.println("Guest Registration Error: " + e.getMessage());
        }
    }

    // ------------------------- EMPLOYEE REGISTRATION -------------------------
    public static void registerEmployee(Scanner scan) {
        try {
            System.out.println("\n--- ADMIN LOGIN REQUIRED ---");
            System.out.print("Enter Admin User ID: ");
            int adminId = scan.nextInt();
            scan.nextLine();

            System.out.print("Enter Admin Password: ");
            String adminPass = scan.nextLine();

            LoginDAO loginDAO = new LoginDAO();
            if (!loginDAO.validateUser(adminId, adminPass, "Admin")) {
                System.out.println("Invalid Admin credentials!");
                return;
            }

            System.out.println("\n--- EMPLOYEE REGISTRATION ---");

            System.out.print("Name: ");
            String name = scan.nextLine();

            System.out.print("Email: ");
            String email = scan.nextLine();

            System.out.print("Phone: ");
            String phone = scan.nextLine();

            System.out.print("Gender: ");
            String gender = scan.nextLine();

            System.out.print("Role ID: ");
            int roleId = scan.nextInt();
            scan.nextLine();

            System.out.print("Department ID: ");
            int deptId = scan.nextInt();
            scan.nextLine();

            System.out.print("Shift: ");
            String shift = scan.nextLine();

            // ------------------------- LOCATION -------------------------
            System.out.println("\n--- LOCATION DETAILS ---");
            System.out.print("Address: ");
            String address = scan.nextLine();
            System.out.print("City: ");
            String city = scan.nextLine();
            System.out.print("State: ");
            String state = scan.nextLine();
            System.out.print("Country: ");
            String country = scan.nextLine();
            System.out.print("Zipcode: ");
            String zipcode = scan.nextLine();
            System.out.print("Nationality: ");
            String nationality = scan.nextLine();

            Location location = new Location(address, city, state, country, zipcode, nationality, LocalDateTime.now().toString());

            LocationDAO locationDAO = new LocationDAO();
            locationDAO.addLocation(location);

            int locationId = locationDAO.getLastInsertedId();

            // ------------------------- USER -------------------------
            UserDAO userDAO = new UserDAO();
            int newUserId = userDAO.createUser(name, email, phone, gender, locationId);

            // ------------------------- EMPLOYEE -------------------------
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employeeDAO.addEmployee(newUserId, roleId, deptId, shift, "EMP-" + newUserId);

            // ------------------------- LOGIN -------------------------
            String userType = (roleId == 1) ? "Admin" : "Receptionist";
            if (roleId == 1 || roleId == 2) {
            	System.out.print("Password: ");
                String password = scan.nextLine();

            	loginDAO.registerLogin(newUserId, password, userType);
            }
            
            System.out.println("Your user id is: " + newUserId);
            System.out.println("Employee registered successfully!");

        } catch (Exception e) {
            System.out.println("Employee Registration Error: " + e.getMessage());
        }
    }

    // ------------------------- LOGIN -------------------------
    public static void login(Scanner scan) {

        try {
            System.out.print("Enter User ID: ");
            int userId = scan.nextInt();
            scan.nextLine();

            System.out.print("Enter Password: ");
            String password = scan.nextLine();

            LoginDAO loginDAO = new LoginDAO();
            ResultSet rs = loginDAO.getLoginByUserId(userId);

            if (!rs.next()) {
                System.out.println("User not found!");
                return;
            }

            if (!rs.getString("password").equals(password)) {
                System.out.println("Invalid password!");
                return;
            }

            String userType = rs.getString("user_type");

            switch (userType) {
                case "Admin":
                	AdminService.adminAccess(scan, userId);
                	break;
                case "Receptionist":
                	ReceptionistService.receptionistAccess(scan, userId);
                	break;
                case "Guest":
                	GuestService.guestAccess(scan, userId);
                	break;
                default:
                	System.out.println("Invalid user role!");
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }
}
