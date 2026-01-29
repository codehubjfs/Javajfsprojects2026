package com.appointment.util;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;
public class OTPGenerator {
    // In-memory storage for OTPs (mobile -> OTP)
    private static Map<String, String> otpStorage = new HashMap<>();
    private static Random random = new Random();
    /*
     * Generate 6-digit OTP for mobile number
     */
    public static String generateOTP(String mobileNumber) {
        // Generate random 6-digit number
        int otp = 100000 + random.nextInt(900000);
        String otpString = String.valueOf(otp);
        
        // Store OTP in memory
        otpStorage.put(mobileNumber, otpString);
        
        return otpString;
    }
    /*
     * Verify OTP
     */
    public static boolean verifyOTP(String mobileNumber, String enteredOTP) {
        String storedOTP = otpStorage.get(mobileNumber);
        
        if (storedOTP != null && storedOTP.equals(enteredOTP)) {
            // Clear OTP after successful verification
            otpStorage.remove(mobileNumber);
            return true;
        }
        return false;
    }
    /*
     * Clear OTP for mobile number
     */
    public static void clearOTP(String mobileNumber) {
        otpStorage.remove(mobileNumber);
    }
    /**
     * Display OTP in console (simulates SMS)
     */
    public static void displayOTP(String mobileNumber, String otp) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" SMS SIMULATION");
        System.out.println("=".repeat(50));
        System.out.println("To: " + mobileNumber);
        System.out.println("Message: Your OTP is: " + otp);
        System.out.println("Valid for: 5 minutes");
        System.out.println("=".repeat(50) + "\n");
    }
}