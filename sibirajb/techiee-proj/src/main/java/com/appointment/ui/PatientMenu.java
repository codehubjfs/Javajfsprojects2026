
package com.appointment.ui;

import com.appointment.model.*;
import com.appointment.service.PatientService;
import com.appointment.util.InputValidator;

import java.util.List;
import java.util.Scanner;

public class PatientMenu {
    private Scanner scanner;
    private PatientService patientService;
    private User currentUser;
    private Patient currentPatient;

    public PatientMenu(User user) {
        this.scanner = new Scanner(System.in);
        this.patientService = new PatientService();
        this.currentUser = user;
        this.currentPatient = patientService.getOrCreatePatient(user.getUserId());
    }
    /**
     * Show patient menu
     */
    public void show() {
        if (currentPatient == null) {
            System.out.println(" Patient profile not found!");
            return;
        }

        while (true) {
            displayMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewSpecializations();
                    break;
                case 2:
                    bookAppointment();
                    break;
                case 3:
                    viewMyAppointments();
                    break;
                case 4:
                    viewPrescriptions();
                    break;
                case 5:
                    makePayment();
                    break;
                case 6:
                    giveReview();
                    break;
                case 7:
                    System.out.println("\n Logging out...");
                    return;
                default:
                    System.out.println(" Invalid choice!");
            }
        }
    }

    /**
     * Display patient menu
     */
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                     PATIENT MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. View Specializations");
        System.out.println("2. Book Appointment");
        System.out.println("3. View My Appointments");
        System.out.println("4. View Prescriptions");
        System.out.println("5. Make Payment");
        System.out.println("6. Give Review");
        System.out.println("7. Logout");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");
    }
    /**
     * View specializations
     */
    private void viewSpecializations() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    AVAILABLE SPECIALIZATIONS");
        System.out.println("=".repeat(70));
        
        List<Specialization> specializations = patientService.getAllSpecializations();
        
        if (specializations.isEmpty()) {
            System.out.println("No specializations available!");
            return;
        }
        
        System.out.printf("%-5s %-25s %-40s\n", "ID", "Name", "Description");
        System.out.println("-".repeat(70));
        
        for (Specialization spec : specializations) {
            System.out.printf("%-5d %-25s %-40s\n",
                spec.getSpecializationId(),
                spec.getName(),
                spec.getDescription());
        }
        System.out.println("=".repeat(70));
    }
    /**
     * Book appointment
     */
    private void bookAppointment() {
        System.out.println("\n--- Book Appointment ---");
        // Show specializations
        List<Specialization> specializations = patientService.getAllSpecializations();
        if (specializations.isEmpty()) {
            System.out.println(" No specializations available!");
            return;
        }
        
        System.out.println("\nAvailable Specializations:");
        for (Specialization spec : specializations) {
            System.out.printf("%d. %s\n", spec.getSpecializationId(), spec.getName());
        }
        
        System.out.print("\nSelect Specialization ID: ");
        int specializationId = getIntInput();
        
        // Show doctors in that specialization
        List<Doctor> doctors = patientService.getDoctorsBySpecialization(specializationId);
        if (doctors.isEmpty()) {
            System.out.println(" No available doctors in this specialization!");
            return;
        }
        
        System.out.println("\nAvailable Doctors:");
        System.out.printf("%-5s %-25s %-10s %-12s\n", "ID", "Name", "Fees", "Experience");
        System.out.println("-".repeat(55));
        
        for (Doctor doctor : doctors) {
            System.out.printf("%-5d %-25s ₹%-10.2f %d years\n",
                doctor.getDoctorId(),
                doctor.getUser().getName(),
                doctor.getFees(),
                doctor.getExperience());
        }
        
        System.out.print("\nSelect Doctor ID: ");
        int doctorId = getIntInput();
        
        // Find selected doctor
        Doctor selectedDoctor = doctors.stream()
            .filter(d -> d.getDoctorId() == doctorId)
            .findFirst()
            .orElse(null);
        
        if (selectedDoctor == null) {
            System.out.println(" Invalid doctor ID!");
            return;
        }
        
        // Get appointment details
        String date = "";
        while (!InputValidator.isValidDate(date)) {
            System.out.print("Enter Date (YYYY-MM-DD): ");
            date = scanner.nextLine().trim();
            if (!InputValidator.isValidDate(date)) {
                System.out.println(" Invalid date format!");
            }
        }
        
        String time = "";
        while (!InputValidator.isValidTime(time)) {
            System.out.print("Enter Time (HH:mm, e.g., 14:30): ");
            time = scanner.nextLine().trim();
            if (!InputValidator.isValidTime(time)) {
                System.out.println(" Invalid time format!");
            }
        }
        
        System.out.print("Enter Symptoms/Reason: ");
        String symptoms = scanner.nextLine().trim();
        
        // Book appointment
        if (patientService.bookAppointment(currentPatient.getPatientId(), doctorId, date, time, symptoms)) {
            System.out.println("\n Appointment Details:");
            System.out.println("Doctor: " + selectedDoctor.getUser().getName());
            System.out.println("Specialization: " + selectedDoctor.getSpecialization().getName());
            System.out.println("Fees: ₹" + selectedDoctor.getFees());
            System.out.println("Date: " + date);
            System.out.println("Time: " + time);
            System.out.println("\n Please make payment to confirm the appointment.");
        }
    }

    /**
     * View my appointments
     */
    private void viewMyAppointments() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("                           MY APPOINTMENTS");
        System.out.println("=".repeat(90));
        
        List<Appointment> appointments = patientService.getAppointments(currentPatient.getPatientId());
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found!");
            return;
        }
        
        System.out.printf("%-5s %-12s %-10s %-12s %-30s %-12s\n", 
            "ID", "Date", "Time", "Doctor ID", "Symptoms", "Status");
        System.out.println("-".repeat(90));
        
        for (Appointment apt : appointments) {
            System.out.printf("%-5d %-12s %-10s %-12d %-30s %-12s\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getDoctorId(),
                apt.getSymptoms() != null ? apt.getSymptoms().substring(0, Math.min(30, apt.getSymptoms().length())) : "N/A",
                apt.getStatus());
        }
        System.out.println("=".repeat(90));
    }

    /**
     * View prescriptions
     */
    private void viewPrescriptions() {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("                           MY PRESCRIPTIONS");
        System.out.println("=".repeat(90));
        
        List<Prescription> prescriptions = patientService.getPrescriptions(currentPatient.getPatientId());
        
        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions found!");
            return;
        }
        
        for (Prescription prescription : prescriptions) {
            System.out.println("\n" + "-".repeat(90));
            System.out.println("Prescription ID: " + prescription.getPrescriptionId());
            System.out.println("Appointment ID: " + prescription.getAppointmentId());
            System.out.println("Diagnosis: " + prescription.getDiagnosis());
            System.out.println("Notes/Medicines: " + prescription.getNotes());
            System.out.println("-".repeat(90));
        }
    }

    /**
     * Make payment
     */
    private void makePayment() {
        System.out.println("\n--- Make Payment ---");
        
        // Show scheduled appointments without payment
        List<Appointment> appointments = patientService.getAppointments(currentPatient.getPatientId());
        List<Appointment> pendingPayments = appointments.stream()
            .filter(a -> a.getStatus().equals("SCHEDULED"))
            .collect(java.util.stream.Collectors.toList());
        
        if (pendingPayments.isEmpty()) {
            System.out.println("No pending payments!");
            return;
        }
        
        System.out.println("\nAppointments Pending Payment:");
        System.out.printf("%-5s %-12s %-10s %-12s\n", "ID", "Date", "Time", "Doctor ID");
        System.out.println("-".repeat(50));
        
        for (Appointment apt : pendingPayments) {
            System.out.printf("%-5d %-12s %-10s %-12d\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getAppointmentTime(),
                apt.getDoctorId());
        }
        
        System.out.print("\nEnter Appointment ID: ");
        int appointmentId = getIntInput();
        
        // Find the appointment
        Appointment selectedApt = pendingPayments.stream()
            .filter(a -> a.getAppointmentId() == appointmentId)
            .findFirst()
            .orElse(null);
        
        if (selectedApt == null) {
            System.out.println(" Invalid appointment ID!");
            return;
        }
        
        System.out.print("Enter Amount: ₹");
        double amount = getDoubleInput();
        
        if (amount <= 0) {
            System.out.println(" Invalid amount!");
            return;
        }
        
        patientService.makePayment(appointmentId, currentPatient.getPatientId(), amount);
    }

    /**
     * Give review
     */
    private void giveReview() {
        System.out.println("\n--- Give Review ---");
        
        // Show completed appointments
        List<Appointment> appointments = patientService.getAppointments(currentPatient.getPatientId());
        List<Appointment> completedAppointments = appointments.stream()
            .filter(a -> a.getStatus().equals("COMPLETED"))
            .collect(java.util.stream.Collectors.toList());
        
        if (completedAppointments.isEmpty()) {
            System.out.println("No completed appointments to review!");
            return;
        }
        
        System.out.println("\nCompleted Appointments:");
        System.out.printf("%-5s %-12s %-12s\n", "ID", "Date", "Doctor ID");
        System.out.println("-".repeat(35));
        
        for (Appointment apt : completedAppointments) {
            System.out.printf("%-5d %-12s %-12d\n",
                apt.getAppointmentId(),
                apt.getAppointmentDate(),
                apt.getDoctorId());
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
        
        int rating = 0;
        while (rating < 1 || rating > 5) {
            System.out.print("Enter Rating (1-5): ");
            rating = getIntInput();
            if (!InputValidator.isValidRating(rating)) {
                System.out.println(" Rating must be between 1 and 5!");
            }
        }
        
        System.out.print("Enter Comment: ");
        String comment = scanner.nextLine().trim();
        
        patientService.giveReview(
            appointmentId, 
            currentPatient.getPatientId(), 
            selectedApt.getDoctorId(), 
            rating, 
            comment
        );
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