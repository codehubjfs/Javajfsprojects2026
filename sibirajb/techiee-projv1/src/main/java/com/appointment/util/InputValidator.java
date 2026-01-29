package com.appointment.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputValidator {
    
    /**
     * Validate mobile number (10 digits)
     */
    public static boolean isValidMobileNumber(String mobile) {
        return mobile != null && mobile.matches("\\d{10}");
    }
    /**
     * Validate OTP (6 digits)
     */
    public static boolean isValidOTP(String otp) {
        return otp != null && otp.matches("\\d{6}");
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z]");
    }   
    /**
     * Validate date format (yyyy-MM-dd)
     */
    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date parsedDate = sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
   
    /**
     * Validate time format (HH:mm)
     */
    public static boolean isValidTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            Date parsedTime = sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    /**
     * Validate rating (1-5)
     */
    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

    /**
     * Check if string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}