package com.appointment.ui;

import com.appointment.model.User;

public class MenuFactory {

    /**
     * Get appropriate menu based on user role
     */
    public static void showRoleBasedMenu(User user) {
        String role = user.getRole();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Role: " + role + " | Name: " + user.getName());
        System.out.println("=".repeat(60));
        
        switch (role) {
            case "ADMIN":
                AdminMenu adminMenu = new AdminMenu(user);
                adminMenu.show();
                break;
            case "DOCTOR":
                DoctorMenu doctorMenu = new DoctorMenu(user);
                doctorMenu.show();
                break;
            case "PATIENT":
                PatientMenu patientMenu = new PatientMenu(user);
                patientMenu.show();
                break;
            default:
                System.out.println(" Invalid role!");
        }
    }
}