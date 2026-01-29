
package com.appointment.ui;

import com.appointment.model.*;
import com.appointment.service.DoctorService;

import java.util.List;
import java.util.Scanner;

public class DoctorMenu {
    private Scanner scanner;
    private DoctorService doctorService;
    private User currentUser;
    private Doctor currentDoctor;

    public DoctorMenu(User user) {
        this.scanner = new Scanner(System.in);
        this.doctorService = new DoctorService();
        this.currentUser = user;
        this.currentDoctor = doctorService.getDoctorByUserId(user.getUserId());
    }

    /**
     * Show doctor menu
     */
    public void show() {
        if (currentDoctor == null) {
            System.out.println(" Doctor profile not found!");
            return;
        }

        while (true) {
            displayMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewAppointments();
                    break;
                case 2:
                    updateAvailability();
                    break;
                case 3:
                    writePrescription();
                    break;
                case 4:
                    viewReviews();
                    break;
                case 5:
                    System.out.println("\n Logging out...");
                    return;
                default:
                    System.out.println(" Invalid choice!");
            }
        }
    }

    /**
     * Display doctor menu
     */
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                     DOCTOR MENU");
        System.out.println("=".repeat(60));
        System.out.println("Specialization: " + currentDoctor.getSpecialization().getName());
        System.out.println("Availability: " + currentDoctor.getAvailability());
        System.out.println("-".repeat(60));
        System.out.println("1. View Appointments");
        System.out.println("2. Update Availability");
        System.out.println("3. Write Prescription");
        System.out.println("4. View Reviews");
        System.out.println("5. Logout");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");
    }

    /**
     * View appointments
     */
    private void viewAppointments() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("                           MY APPOINTMENTS"); 
        System.out.println("=".repeat(90));
        
        List<Appointment> appointments = doctorService.getAppointments(currentDoctor.getDoctorId());
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found!");
            return;
        }
        
        System.out.printf("%-5s %-12s %-10s %-12s %-30s %-12s\n", 
            "ID", "Date", "Time", "Patient ID", "Symptoms", "Status");
        System.out.println("-".repeat(90));
        
        for (Appointment apt : appointments) {
            System.out.printf("%-5d %-12s %-10s %-12d %-30s %-12s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getPatientId(),
                apt.getSymptoms() != null ? apt.getSymptoms().substring(0, Math.min(30, apt.getSymptoms().length())) : "N/A",
                apt.getStatus());
        }
        System.out.println("=".repeat(90));
    }

    /**
     * Update availability
     */
    private void updateAvailability() {
        System.out.println("\n--- Update Availability ---");
        System.out.println("Current Status: " + currentDoctor.getAvailability());
        System.out.println("\n1. AVAILABLE");
        System.out.println("2. NOT_AVAILABLE");
        System.out.print("\nSelect new status: ");
        
        int choice = getIntInput();
        String newStatus = (choice == 1) ? "AVAILABLE" : "NOT_AVAILABLE";
        
        if (doctorService.updateAvailability(currentDoctor.getDoctorId(), newStatus)) {
            currentDoctor.setAvailability(newStatus);
        }
    }

    /**
     * Write prescription
     */
    private void writePrescription() {
        System.out.println("\n--- Write Prescription ---");
        
        // First show scheduled appointments
        List<Appointment> appointments = doctorService.getAppointments(currentDoctor.getDoctorId());
        List<Appointment> scheduledAppointments = appointments.stream()
            .filter(a -> a.getStatus().equals("SCHEDULED"))
            .collect(java.util.stream.Collectors.toList());
        
        if (scheduledAppointments.isEmpty()) {
            System.out.println("No scheduled appointments to write prescription for!");
            return;
        }
        
        System.out.println("\nScheduled Appointments:");
        System.out.printf("%-5s %-12s %-10s %-12s %-30s\n", 
            "ID", "Date", "Time", "Patient ID", "Symptoms");
        System.out.println("-".repeat(70));
        
        for (Appointment apt : scheduledAppointments) {
            System.out.printf("%-5d %-12s %-10s %-12d %-30s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getPatientId(),
                apt.getSymptoms() != null ? apt.getSymptoms() : "N/A");
        }
        
        System.out.print("\nEnter Appointment ID: ");
        int appointmentId = getIntInput();
        
        // Find the appointment
        Appointment selectedApt = scheduledAppointments.stream()
            .filter(a -> a.getAppointmentId() == appointmentId)
            .findFirst()
            .orElse(null);
        
        if (selectedApt == null) {
            System.out.println(" Invalid appointment ID!");
            return;
        }
        
        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine().trim();
        
        System.out.print("Enter Medical Notes/Prescriptions: ");
        String notes = scanner.nextLine().trim();
        
        doctorService.writePrescription(
            appointmentId, 
            currentDoctor.getDoctorId(), 
            selectedApt.getPatientId(), 
            diagnosis, 
            notes
        );
    }

    /**
     * View reviews
     */
    private void viewReviews() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                           MY REVIEWS");
        System.out.println("=".repeat(80));
        
        List<Review> reviews = doctorService.getReviews(currentDoctor.getDoctorId());
        
        if (reviews.isEmpty()) {
            System.out.println("No reviews yet!");
            return;
        }
        
        System.out.printf("%-5s %-15s %-10s %-40s\n", 
            "ID", "Appointment ID", "Rating", "Comment");
        System.out.println("-".repeat(80));
        
        for (Review review : reviews) {
            System.out.printf("%-5d %-15d %-10s %-40s\n",
                review.getReviewId(),
                review.getAppointmentId(),
                "⭐".repeat(review.getRating()),
                review.getComment() != null ? review.getComment().substring(0, Math.min(40, review.getComment().length())) : "");
        }
        System.out.println("=".repeat(80));
        
        // Calculate average rating
        double avgRating = reviews.stream()
            .mapToInt(Review::getRating)
            .average()
            .orElse(0.0);
        
        System.out.printf("\n  Average Rating:%.2f/5.0(%d reviews)\n", avgRating, reviews.size());
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
}