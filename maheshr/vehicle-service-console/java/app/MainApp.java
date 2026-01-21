package app;

import config.DBConn;
import controller.AuthController;
import controller.CustomerController;
import model.User;

public class MainApp {
    
    public static void main(String[] args) {
        System.out.println("Vehicle Service Management");
        
        try {
            DBConn.getConnection();
            
            AuthController authController = new AuthController();
            User loggedInUser = authController.showLoginMenu();
            
            if (loggedInUser != null) {
                switch (loggedInUser.getRole()) {
                    case "CUSTOMER":
                        CustomerController customerController = new CustomerController(loggedInUser);
                        customerController.showDashboard();
                        break;
                    case "ADMIN":
                        System.out.println("\nAdmin module not implemented yet.");
                        break;
                    case "ADVISOR":
                        System.out.println("\nAdvisor module not implemented yet.");
                        break;
                    default:
                        System.out.println("\nUnknown role: " + loggedInUser.getRole());
                }
            }
            
        } catch (Exception e) {
            System.err.println("\nApplication error: " + e.getMessage());
            e.printStackTrace();
        } finally {
//            DBConn.closeConnection();
            System.out.println("\nApplication terminated successfully.");
        }
    }
}
