
package com.appointment.ui;

import com.appointment.model.*;
import com.appointment.service.AdminService;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private Scanner scanner;
    private AdminService adminService;
    private User currentUser;

    public AdminMenu(User user) {
        this.scanner = new Scanner(System.in);
        this.adminService = new AdminService();
        this.currentUser = user;
    }
    /* Show admin menu */   
    public void show() {
        while (true) {
            displayMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addDoctor();
                    break;
                case 2:
                    viewDoctors();
                    break;
                case 3:
                    addSpecialization();
                    break;
                case 4:
                    viewSpecializations();
                    break;
                case 5:
                    viewAppointments();
                    break;
                case 6:
                    System.out.println("\n Logging out...");
                    return;
                default:
                    System.out.println(" Invalid choice!");
            }
        }
    }
    /* Display admin menu*/
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                      ADMIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Add Doctor");
        System.out.println("2. View Doctors");
        System.out.println("3. Add Specialization");
        System.out.println("4. View Specializations");
        System.out.println("5. View All Appointments");
        System.out.println("6. Logout");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");
    }
    /*Add new doctor  */
    private void addDoctor() {
        System.out.println("\n--- Add New Doctor ---");
        
        System.out.print("Enter Doctor Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Mobile Number: ");
        String mobile = scanner.nextLine().trim();
        
        // Show specializations
        List<Specialization> specializations = adminService.getAllSpecializations();
        if (specializations.isEmpty()) {
            System.out.println(" No specializations available! Please add specialization first.");
            return;
        }
        
        System.out.println("\nAvailable Specializations:");
        for (Specialization spec : specializations) {
            System.out.printf("%d. %s - %s\n", 
                spec.getSpecializationId(), 
                spec.getName(), 
                spec.getDescription());
        }
        
        System.out.print("\nSelect Specialization ID: ");
        int specializationId = getIntInput();
        
        System.out.print("Enter Consultation Fees: ");
        double fees = getDoubleInput();
        
        System.out.print("Enter Years of Experience: ");
        int experience = getIntInput();
        
        adminService.addDoctor(name, mobile, specializationId, fees, experience, currentUser.getUserId());
    }

    /**
     * View all doctors
     */
    private void viewDoctors() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                           ALL DOCTORS");
        System.out.println("=".repeat(80));
        
        List<Doctor> doctors = adminService.getAllDoctors();
        
        if (doctors.isEmpty()) {
            System.out.println("No doctors found!");
            return;
        }
        
        System.out.printf("%-5s %-20s %-15s %-15s %-10s %-8s %-12s\n", 
            "ID", "Name", "Mobile", "Specialization", "Fees", "Exp", "Availability");
        System.out.println("-".repeat(80));
        
        for (Doctor doctor : doctors) {
            System.out.printf("%-5d %-20s %-15s %-15s %-10.2f %-8d %-12s\n",
                doctor.getDoctorId(),
                doctor.getUser().getName(),
                doctor.getUser().getMobileNumber(),
                doctor.getSpecialization().getName(),
                doctor.getFees(),
                doctor.getExperience(),
                doctor.getAvailability());
        }
        System.out.println("=".repeat(80));
    }

    /**
     * Add new specialization
     */
    private void addSpecialization() {
        System.out.println("\n--- Add New Specialization ---");
        
        System.out.print("Enter Specialization Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine().trim();
        
        adminService.addSpecialization(name, description);
    }

    /**
     * View all specializations
     */
    private void viewSpecializations() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    ALL SPECIALIZATIONS");
        System.out.println("=".repeat(70));
        
        List<Specialization> specializations = adminService.getAllSpecializations();
        
        if (specializations.isEmpty()) {
            System.out.println("No specializations found!");
            return;
        }
        
        System.out.printf("%-5s %-20s %-40s\n", "ID", "Name", "Description");
        System.out.println("-".repeat(70));
        
        for (Specialization spec : specializations) {
            System.out.printf("%-5d %-20s %-40s\n",
                spec.getSpecializationId(),
                spec.getName(),
                spec.getDescription());
        }
        System.out.println("=".repeat(70));
    }

    /**
     * View all appointments
     */
    private void viewAppointments() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("                           ALL APPOINTMENTS");
        System.out.println("=".repeat(90));
        
        List<Appointment> appointments = adminService.getAllAppointments();
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found!");
            return;
        }
        
        System.out.printf("%-5s %-12s %-10s %-10s %-12s %-20s\n", 
            "ID", "Date", "Time", "Patient", "Doctor", "Status");
        System.out.println("-".repeat(90));
        
        for (Appointment apt : appointments) {
            System.out.printf("%-5d %-12s %-10s %-10s %-10s %-20s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                "P-" + apt.getPatientId(),
                "D-" + apt.getDoctorId(),
                apt.getStatus());
        }
        System.out.println("=".repeat(90));
    }

    /**
     * Get integer input
     */
    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Get double input
     */
    private double getDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}