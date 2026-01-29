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
    private static final int SLOT_CAPACITY = 5; // Maximum patients per time slot

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
            System.out.println("Patient profile not found!");
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
                    System.out.println("\nLogging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Please Enter a valid choice");
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
     * Book appointment with slot capacity check
     */
    private void bookAppointment() {
        System.out.println("\n--- Book Appointment ---");
        
        // Show specializations
        List<Specialization> specializations = patientService.getAllSpecializations();
        if (specializations.isEmpty()) {
            System.out.println("No specializations available!");
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
            System.out.println("No available doctors in this specialization!");
            return;
        }
        
        System.out.println("\nAvailable Doctors:");
        System.out.printf("%-5s %-25s %-10s %-12s %-15s\n", "ID", "Name", "Fees", "Experience", "Status");
        System.out.println("-".repeat(70));
        
        for (Doctor doctor : doctors) {
            System.out.printf("%-5d %-25s ₹%-10.2f %d years   %-15s\n",
                doctor.getDoctorId(),
                doctor.getUser().getName(),
                doctor.getFees(),
                doctor.getExperience(),
                doctor.getAvailability());
        }
        
        System.out.print("\nSelect Doctor ID: ");
        int doctorId = getIntInput();
        
        // Find selected doctor
        Doctor selectedDoctor = doctors.stream()
            .filter(d -> d.getDoctorId() == doctorId)
            .findFirst()
            .orElse(null);
        
        if (selectedDoctor == null) {
            System.out.println("Invalid doctor ID!");
            return;
        }
        
        // Check if doctor is available
        if (!"AVAILABLE".equals(selectedDoctor.getAvailability())) {
            System.out.println("Doctor is currently not available.");         
            System.out.println("Please select another doctor.");
            return;
        }
        
        // Get appointment date
        String date = "";
        while (true) {
            System.out.print("\nEnter Date (YYYY-MM-DD): ");
            date = scanner.nextLine().trim();
            
            if (!InputValidator.isValidDate(date)) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD format.");
                continue;
            }
            
            if (!InputValidator.isTodayOrFutureDate(date)) {
                System.out.println("Date must be today or in the future!");
                continue;
            }
            
            break;
        }
        
        // Show available slots with capacity info
        System.out.println("\nNote: Each time slot can accommodate maximum " + SLOT_CAPACITY + " patients");
        showAvailableTimeSlotsWithCapacity(doctorId, date);
        
        // Get appointment time with comprehensive validation
        String time = "";
        boolean slotAvailable = false;
        int timeAttempts = 0;
        int maxAttempts = 5;
        
        while (!slotAvailable && timeAttempts < maxAttempts) {
            System.out.print("\nEnter Time (HH:mm, e.g., 09:30, 14:00): ");
            time = scanner.nextLine().trim();
            
            // Validate time format
            if (!InputValidator.isValidTime(time)) {
                timeAttempts++;
                System.out.println(" Invalid time format! Please use HH:mm format (e.g., 09:30, 14:00).");
                System.out.println("Attempts remaining: " + (maxAttempts - timeAttempts));
                
                if (timeAttempts < maxAttempts) {
                    System.out.print("Show available time slots? (y/n): ");
                    String showSlots = scanner.nextLine().trim().toLowerCase();
                    if (showSlots.equals("y")) {
                        showAvailableTimeSlotsWithCapacity(doctorId, date);
                    }
                }
                continue;
            }
            
            // Check if time is within working hours
            if (!isWithinWorkingHours(time)) {
                timeAttempts++;
                System.out.println(" Time is outside working hours!");
                System.out.println("Working hours: 9:00 AM - 1:00 PM and 2:00 PM - 6:00 PM");
                System.out.println("Attempts remaining: " + (maxAttempts - timeAttempts));
                
                if (timeAttempts < maxAttempts) {
                    System.out.print("Show available time slots? (y/n): ");
                    String showSlots = scanner.nextLine().trim().toLowerCase();
                    if (showSlots.equals("y")) {
                        showAvailableTimeSlotsWithCapacity(doctorId, date);
                    }
                }
                continue;
            }
            
            // Check if time is in the future
            if (!InputValidator.isFutureDateTime(date, time)) {
                timeAttempts++;
                System.out.println(" Appointment time must be in the future!");
                System.out.println("Attempts remaining: " + (maxAttempts - timeAttempts));
                
                if (timeAttempts < maxAttempts) {
                    System.out.print("Show available time slots? (y/n): ");
                    String showSlots = scanner.nextLine().trim().toLowerCase();
                    if (showSlots.equals("y")) {
                        showAvailableTimeSlotsWithCapacity(doctorId, date);
                    }
                }
                continue;
            }
            
            // Check slot capacity
            int currentBookings = patientService.getSlotBookingCount(doctorId, date, time);
            
            if (currentBookings >= SLOT_CAPACITY) {
                timeAttempts++;
                System.out.println(" This slot is fully booked! (" + currentBookings + "/" + SLOT_CAPACITY + " slots filled)");
                System.out.println("Attempts remaining: " + (maxAttempts - timeAttempts));
                
                if (timeAttempts < maxAttempts) {
                    System.out.println("Please choose another time slot.");
                    System.out.print("Show available time slots? (y/n): ");
                    String showSlots = scanner.nextLine().trim().toLowerCase();
                    if (showSlots.equals("y")) {
                        showAvailableTimeSlotsWithCapacity(doctorId, date);
                    }
                }
            } else {
                slotAvailable = true;
                System.out.println(" Slot available! (" + (currentBookings + 1) + "/" + SLOT_CAPACITY + " slots will be filled after booking)");
            }
        }
        
        if (!slotAvailable) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println(" MAXIMUM TIME SLOT ATTEMPTS REACHED!");
            System.out.println("=".repeat(60));
            System.out.println("Please try again with:");
            System.out.println("  • A different date");
            System.out.println("  • A different doctor");
            System.out.println("  • Valid time slots from the available slots list");
            System.out.println("=".repeat(60));
            return;
        }
        
        // Get symptoms
        System.out.print("\nEnter Symptoms/Reason: ");
        String symptoms = scanner.nextLine().trim();
        
        // Confirm booking
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                 CONFIRM APPOINTMENT");
        System.out.println("=".repeat(60));
        System.out.println("Doctor          : " + selectedDoctor.getUser().getName());
        System.out.println("Specialization  : " + selectedDoctor.getSpecialization().getName());
        System.out.println("Fees            : ₹" + selectedDoctor.getFees());
        System.out.println("Date            : " + date);
        System.out.println("Time            : " + time);
        System.out.println("Symptoms        : " + (symptoms.isEmpty() ? "Not specified" : symptoms));
        System.out.println("=".repeat(60));
        
        System.out.print("\nConfirm booking? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (!confirm.equals("y")) {
            System.out.println("Appointment booking cancelled.");
            return;
        }
        
        // Book appointment
        if (patientService.bookAppointment(currentPatient.getPatientId(), doctorId, date, time, symptoms)) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println(" APPOINTMENT BOOKED SUCCESSFULLY!");
            System.out.println("=".repeat(60));
            System.out.println("Your appointment has been confirmed.");
            System.out.println("Date: " + date + " at " + time);
            System.out.println("Please arrive 10 minutes before your appointment time.");
            System.out.println("=".repeat(60));
        } else {
            System.out.println("\n Failed to book appointment!");
            System.out.println("Please try again or contact support.");
        }
    }
    
    /**
     * Check if time is within working hours
     */
    private boolean isWithinWorkingHours(String time) {
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            
            // Convert to minutes for easier comparison
            int timeInMinutes = hour * 60 + minute;
            
            // Morning: 9:00 AM - 1:00 PM (540 - 780 minutes)
            boolean isMorning = timeInMinutes >= 540 && timeInMinutes <= 780;
            
            // Evening: 2:00 PM - 6:00 PM (840 - 1080 minutes)
            boolean isEvening = timeInMinutes >= 840 && timeInMinutes <= 1080;
            
            return isMorning || isEvening;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Show available time slots with capacity information
     */
    private void showAvailableTimeSlotsWithCapacity(int doctorId, String date) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                      AVAILABLE TIME SLOTS");
        System.out.println("=".repeat(80));
        System.out.println("Slot capacity: " + SLOT_CAPACITY + " patients per slot");
        System.out.println("=".repeat(80));
        
        // Morning slots (9:00 AM - 1:00 PM)
        String[] morningSlots = {
            "09:00", "09:30", "10:00", "10:30",
            "11:00", "11:30", "12:00", "12:30"
        };
        
        // Evening slots (2:00 PM - 6:00 PM)
        String[] eveningSlots = {
            "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30"
        };
        
        // Display Morning Slots
        System.out.println("\n--- MORNING SLOTS (9:00 AM - 1:00 PM) ---");
        System.out.printf("%-10s %-15s %-10s\n", "Time", "Status", "Available");
        System.out.println("-".repeat(40));
        
        int morningAvailable = 0;
        for (String time : morningSlots) {
            int bookingCount = patientService.getSlotBookingCount(doctorId, date, time);
            int available = SLOT_CAPACITY - bookingCount;
            String status = available > 0 ? " AVAILABLE" : " FULL";
            
            System.out.printf("%-10s %-15s %-10s\n", 
                time, 
                status, 
                available + "/" + SLOT_CAPACITY);
            
            if (available > 0) morningAvailable++;
        }
        
        System.out.println("\nMorning slots available: " + morningAvailable + "/" + morningSlots.length);
        
        // Display Evening Slots
        System.out.println("\n--- EVENING SLOTS (2:00 PM - 6:00 PM) ---");
        System.out.printf("%-10s %-15s %-10s\n", "Time", "Status", "Available");
        System.out.println("-".repeat(40));
        
        int eveningAvailable = 0;
        for (String time : eveningSlots) {
            int bookingCount = patientService.getSlotBookingCount(doctorId, date, time);
            int available = SLOT_CAPACITY - bookingCount;
            String status = available > 0 ? " AVAILABLE" : "✗ FULL";
            
            System.out.printf("%-10s %-15s %-10s\n", 
                time, 
                status, 
                available + "/" + SLOT_CAPACITY);
            
            if (available > 0) eveningAvailable++;
        }
        
        System.out.println("\nEvening slots available: " + eveningAvailable + "/" + eveningSlots.length);
        System.out.println("=".repeat(80));
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
            System.out.println("Invalid appointment ID!");
            return;
        }
        
        System.out.print("Enter Amount: ₹");
        double amount = getDoubleInput();
        
        if (amount <= 0) {
            System.out.println("Invalid amount!");
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
            System.out.println("Invalid appointment ID!");
            return;
        }
        
        int rating = 0;
        while (rating < 1 || rating > 5) {
            System.out.print("Enter Rating (1-5): ");
            rating = getIntInput();
            if (!InputValidator.isValidRating(rating)) {
                System.out.println("Rating must be between 1 and 5!");
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