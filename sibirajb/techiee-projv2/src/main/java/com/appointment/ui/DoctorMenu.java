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
                    viewTodayAppointments();
                    break;
                case 3:
                    viewUpcomingAppointments();
                    break;
                case 4:
                    updateAppointmentStatus();
                    break;
                case 5:
                    updateAvailability();
                    break;
                case 6:
                    writePrescription();
                    break;
                case 7:
                    viewPrescriptions();
                    break;
                case 8:
                    viewReviews();
                    break;
                case 9:
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
        System.out.println("Doctor: " + currentUser.getName());
        System.out.println("Specialization: " + currentDoctor.getSpecialization().getName());
        System.out.println("Experience: " + currentDoctor.getExperience() + " years");
        System.out.println("Fees: ₹" + currentDoctor.getFees());
        System.out.println("Availability: " + currentDoctor.getAvailability());
        System.out.println("-".repeat(60));
        System.out.println("1. View All Appointments");
        System.out.println("2. View Today's Appointments");
        System.out.println("3. View Upcoming Appointments");
        System.out.println("4. Update Appointment Status");
        System.out.println("5. Update Availability");
        System.out.println("6. Write Prescription");
        System.out.println("7. View My Prescriptions");
        System.out.println("8. View Reviews");
        System.out.println("9. Logout");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");
    }

    /**
     * View all appointments
     */
    private void viewAppointments() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                           ALL APPOINTMENTS"); 
        System.out.println("=".repeat(100));
        
        List<Appointment> appointments = doctorService.getAppointments(currentDoctor.getDoctorId());
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found!");
            return;
        }
        
        System.out.printf("%-5s %-12s %-10s %-12s %-25s %-15s %-12s\n", 
            "ID", "Date", "Time", "Patient ID", "Patient Name", "Symptoms", "Status");
        System.out.println("-".repeat(100));
        
        for (Appointment apt : appointments) {
            // Get patient details
            Patient patient = doctorService.getPatientById(apt.getPatientId());
            String patientName = (patient != null && patient.getUser() != null) ? 
                patient.getUser().getName() : "Unknown";
            
            String symptoms = apt.getSymptoms();
            if (symptoms != null && symptoms.length() > 20) {
                symptoms = symptoms.substring(0, 17) + "...";
            }
            
            System.out.printf("%-5d %-12s %-10s %-12d %-25s %-15s %-12s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getPatientId(),
                patientName,
                symptoms != null ? symptoms : "N/A",
                apt.getStatus());
        }
        System.out.println("=".repeat(100));
    }

    /**
     * View today's appointments
     */
    private void viewTodayAppointments() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                           TODAY'S APPOINTMENTS"); 
        System.out.println("=".repeat(100));
        
        List<Appointment> appointments = doctorService.getTodayAppointments(currentDoctor.getDoctorId());
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments today!");
            return;
        }
        
        System.out.printf("%-5s %-12s %-10s %-12s %-25s %-25s %-12s\n", 
            "ID", "Date", "Time", "Patient ID", "Patient Name", "Symptoms", "Status");
        System.out.println("-".repeat(100));
        
        for (Appointment apt : appointments) {
            // Get patient details
            Patient patient = doctorService.getPatientById(apt.getPatientId());
            String patientName = (patient != null && patient.getUser() != null) ? 
                patient.getUser().getName() : "Unknown";
            
            System.out.printf("%-5d %-12s %-10s %-12d %-25s %-25s %-12s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getPatientId(),
                patientName,
                apt.getSymptoms() != null ? apt.getSymptoms() : "N/A",
                apt.getStatus());
        }
        System.out.println("=".repeat(100));
    }

    /**
     * View upcoming appointments
     */
    private void viewUpcomingAppointments() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                           UPCOMING APPOINTMENTS"); 
        System.out.println("=".repeat(100));
        
        List<Appointment> appointments = doctorService.getUpcomingAppointments(currentDoctor.getDoctorId());
        
        if (appointments.isEmpty()) {
            System.out.println("No upcoming appointments!");
            return;
        }
        
        System.out.printf("%-5s %-12s %-10s %-12s %-25s %-25s %-12s\n", 
            "ID", "Date", "Time", "Patient ID", "Patient Name", "Symptoms", "Status");
        System.out.println("-".repeat(100));
        
        for (Appointment apt : appointments) {
            // Get patient details
            Patient patient = doctorService.getPatientById(apt.getPatientId());
            String patientName = (patient != null && patient.getUser() != null) ? 
                patient.getUser().getName() : "Unknown";
            
            System.out.printf("%-5d %-12s %-10s %-12d %-25s %-25s %-12s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getPatientId(),
                patientName,
                apt.getSymptoms() != null ? apt.getSymptoms() : "N/A",
                apt.getStatus());
        }
        System.out.println("=".repeat(100));
    }

    /**
     * Update appointment status
     */
    private void updateAppointmentStatus() {
        System.out.println("\n--- Update Appointment Status ---");
        
        // First show appointments
        List<Appointment> appointments = doctorService.getAppointments(currentDoctor.getDoctorId());
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found!");
            return;
        }
        
        System.out.println("\nYour Appointments:");
        System.out.printf("%-5s %-12s %-10s %-12s %-25s %-12s\n", 
            "ID", "Date", "Time", "Patient ID", "Symptoms", "Status");
        System.out.println("-".repeat(80));
        
        for (Appointment apt : appointments) {
            String symptoms = apt.getSymptoms();
            if (symptoms != null && symptoms.length() > 20) {
                symptoms = symptoms.substring(0, 17) + "...";
            }
            
            System.out.printf("%-5d %-12s %-10s %-12d %-25s %-12s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getPatientId(),
                symptoms != null ? symptoms : "N/A",
                apt.getStatus());
        }
        
        System.out.print("\nEnter Appointment ID to update: ");
        int appointmentId = getIntInput();
        
        // Find the appointment
        Appointment selectedApt = appointments.stream()
            .filter(a -> a.getAppointmentId() == appointmentId)
            .findFirst()
            .orElse(null);
        
        if (selectedApt == null) {
            System.out.println(" Invalid appointment ID!");
            return;
        }
        
        System.out.println("\nCurrent Status: " + selectedApt.getStatus());
        System.out.println("\nSelect new status:");
        System.out.println("1. SCHEDULED");
        System.out.println("2. CONFIRMED");
        System.out.println("3. CANCELLED");
        System.out.println("4. COMPLETED");
        System.out.println("5. NO_SHOW");
        System.out.print("\nEnter choice (1-5): ");
        int choice = getIntInput();
        
        String newStatus = "";
        switch (choice) {
            case 1:
                newStatus = "SCHEDULED";
                break;
            case 2:
                newStatus = "CONFIRMED";
                break;
            case 3:
                newStatus = "CANCELLED";
                break;
            case 4:
                newStatus = "COMPLETED";
                break;
            case 5:
                newStatus = "NO_SHOW";
                break;
            default:
                System.out.println(" Invalid choice!");
                return;
        }
        
        if (doctorService.updateAppointmentStatus(appointmentId, newStatus)) {
            System.out.println(" Appointment status updated to: " + newStatus);
        } else {
            System.out.println(" Failed to update appointment status!");
        }
    }

    /**
     * Update availability
     */
    private void updateAvailability() {
        System.out.println("\n--- Update Availability ---");
        System.out.println("Current Status: " + currentDoctor.getAvailability());
        System.out.println("\n1. AVAILABLE");
        System.out.println("2. NOT_AVAILABLE");
        System.out.println("3. ON_LEAVE");
        System.out.print("\nSelect new status: ");
        
        int choice = getIntInput();
        String newStatus = "";
        
        switch (choice) {
            case 1:
                newStatus = "AVAILABLE";
                break;
            case 2:
                newStatus = "NOT_AVAILABLE";
                break;
            case 3:
                newStatus = "ON_LEAVE";
                break;
            default:
                System.out.println(" Invalid choice!");
                return;
        }
        
        if (doctorService.updateAvailability(currentDoctor.getDoctorId(), newStatus)) {
            currentDoctor.setAvailability(newStatus);
            System.out.println(" Availability updated to: " + newStatus);
        } else {
            System.out.println(" Failed to update availability!");
        }
    }

    /**
     * Write prescription
     */
    private void writePrescription() {
        System.out.println("\n--- Write Prescription ---");
        
        // First show completed appointments without prescription
        List<Appointment> appointments = doctorService.getAppointments(currentDoctor.getDoctorId());
        List<Appointment> completedAppointments = appointments.stream()
            .filter(a -> a.getStatus().equals("COMPLETED"))
            .collect(java.util.stream.Collectors.toList());
        
        if (completedAppointments.isEmpty()) {
            System.out.println("No completed appointments to write prescription for!");
            return;
        }
        
        System.out.println("\nCompleted Appointments (without prescription):");
        System.out.printf("%-5s %-12s %-10s %-12s %-25s %-25s\n", 
            "ID", "Date", "Time", "Patient ID", "Patient Name", "Symptoms");
        System.out.println("-".repeat(90));
        
        for (Appointment apt : completedAppointments) {
            // Get patient details
            Patient patient = doctorService.getPatientById(apt.getPatientId());
            String patientName = (patient != null && patient.getUser() != null) ? 
                patient.getUser().getName() : "Unknown";
            
            System.out.printf("%-5d %-12s %-10s %-12d %-25s %-25s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getPatientId(),
                patientName,
                apt.getSymptoms() != null ? apt.getSymptoms() : "N/A");
        }
        
        System.out.print("\nEnter Appointment ID: ");
        int appointmentId = getIntInput();
        
        // Find the appointment
        Appointment selectedApt = completedAppointments.stream()
            .filter(a -> a.getAppointmentId() == appointmentId)
            .findFirst()
            .orElse(null);
        
        if (selectedApt == null) {
            System.out.println(" Invalid appointment ID!");
            return;
        }
        
        // Get patient details for the prescription header
        Patient patient = doctorService.getPatientById(selectedApt.getPatientId());
        String patientName = (patient != null && patient.getUser() != null) ? 
            patient.getUser().getName() : "Unknown";
        String patientAge = (patient != null) ? String.valueOf(patient.getAge()) : "N/A";
        String patientGender = (patient != null) ? patient.getGender() : "N/A";
        
        System.out.println("\n--- Patient Details ---");
        System.out.println("Name: " + patientName);
        System.out.println("Age: " + patientAge);
        System.out.println("Gender: " + patientGender);
        System.out.println("Appointment Date: " + selectedApt.getAppointmentDate());
        System.out.println("Symptoms: " + (selectedApt.getSymptoms() != null ? selectedApt.getSymptoms() : "N/A"));
        System.out.println("-".repeat(40));
        
        System.out.print("\nEnter Diagnosis: ");
        String diagnosis = scanner.nextLine().trim();
        
        System.out.println("Enter Medical Notes/Prescriptions (type 'END' on a new line to finish):");
        StringBuilder notes = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            notes.append(line).append("\n");
        }
        
        if (doctorService.createPrescription(appointmentId, diagnosis, notes.toString())) {
            System.out.println("\n Prescription created successfully!");
        } else {
            System.out.println(" Failed to create prescription!");
        }
    }

    /**
     * View my prescriptions
     */
    private void viewPrescriptions() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("                           MY PRESCRIPTIONS");
        System.out.println("=".repeat(100));
        
        List<Prescription> prescriptions = doctorService.getPrescriptions(currentDoctor.getDoctorId());
        
        if (prescriptions == null || prescriptions.isEmpty()) {
            System.out.println("No prescriptions found!");
            return;
        }
        
        for (Prescription prescription : prescriptions) {
            System.out.println("\n" + "-".repeat(100));
            System.out.println("PRESCRIPTION ID: " + prescription.getPrescriptionId());
            System.out.println("APPOINTMENT ID: " + prescription.getAppointmentId());
            
            // Get patient details for the prescription
            Patient patient = doctorService.getPatientById(prescription.getPatientId());
            if (patient != null && patient.getUser() != null) {
                System.out.println("PATIENT: " + patient.getUser().getName() + 
                                 " (ID: " + patient.getPatientId() + ")");
                if (patient.getAge() > 0) {
                    System.out.println("AGE: " + patient.getAge());
                }
                if (patient.getGender() != null && !patient.getGender().isEmpty()) {
                    System.out.println("GENDER: " + patient.getGender());
                }
            } else {
                System.out.println("PATIENT ID: " + prescription.getPatientId());
            }
            
            System.out.println("DOCTOR: " + currentUser.getName());
            System.out.println("SPECIALIZATION: " + currentDoctor.getSpecialization().getName());
            
            // Get appointment details
            Appointment appointment = doctorService.getAppointmentById(prescription.getAppointmentId());
            if (appointment != null) {
                System.out.println("APPOINTMENT DATE: " + appointment.getAppointmentDate());
                System.out.println("SYMPTOMS: " + 
                    (appointment.getSymptoms() != null ? appointment.getSymptoms() : "N/A"));
            }
            
            System.out.println("DIAGNOSIS: " + prescription.getDiagnosis());
            System.out.println("\nPRESCRIPTION NOTES:");
            System.out.println("-".repeat(40));
            System.out.println(prescription.getNotes() != null ? prescription.getNotes() : "No notes provided");
            System.out.println("-".repeat(100));
            
            // Add prescription footer
            System.out.println("\nDoctor's Signature: ___________________");
            System.out.println("Date: " + java.time.LocalDate.now());
            System.out.println("-".repeat(100));
        }
    }
    /**
     * View reviews
     */
    private void viewReviews() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                           PATIENT REVIEWS");
        System.out.println("=".repeat(80));

        List<Review> reviews = doctorService.getReviews(currentDoctor.getDoctorId());

        if (reviews.isEmpty()) {
            System.out.println("No reviews yet!");
            return;
        }

        // Display reviews
        for (Review review : reviews) {
            System.out.println("\nReview ID: " + review.getReviewId());
            System.out.println("Appointment ID: " + review.getAppointmentId());
            System.out.println("Patient ID: " + review.getPatientId());
            System.out.println("Rating: " + ".".repeat(review.getRating()) + " (" + review.getRating() + "/5)");
            System.out.println("Comment: " + (review.getComment() != null ? review.getComment() : "No comment"));
            System.out.println("-".repeat(80));
        }

        // Statistics
        double avgRating = 0;
        for (Review r : reviews) {
            avgRating += r.getRating();
        }
        avgRating = avgRating / reviews.size();

        System.out.println("\nTotal Reviews: " + reviews.size());
        System.out.printf("Average Rating: %.1f/5.0\n", avgRating);
        System.out.println("=".repeat(80));
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