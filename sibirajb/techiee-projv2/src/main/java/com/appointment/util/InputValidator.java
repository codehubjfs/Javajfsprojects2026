package com.appointment.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputValidator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Validate mobile number (10 digits starting with 6, 7, 8, or 9)
     */
    public static boolean isValidMobileNumber(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            return false;
        }
        // Must be 10 digits and start with 6, 7, 8, or 9
        return mobile.matches("^[6-9][0-9]{9}$");
    }

    /**
     * Get detailed error message for mobile number validation
     */
    public static String getMobileValidationMessage(String mobile) {
        if (mobile == null || mobile.isEmpty()) {
            return "Mobile number cannot be empty.";
        }
        if (mobile.length() != 10) {
            return "Mobile number must be exactly 10 digits.";
        }
        if (!mobile.matches("\\d+")) {
            return "Mobile number must contain only digits.";
        }
        char firstDigit = mobile.charAt(0);
        if (firstDigit < '6' || firstDigit > '9') {
            return "Mobile number must start with digits 6, 7, 8, or 9.";
        }
        return "Valid mobile number.";
    }

    /**
     * Validate OTP (6 digits)
     */
    public static boolean isValidOTP(String otp) {
        return otp != null && otp.matches("\\d{6}");
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z ]+$");
    }

    /**
     * Validate date format (yyyy-MM-dd)
     */
    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validate today or future date
     */
    public static boolean isTodayOrFutureDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date, DATE_FORMATTER);
            LocalDate today = LocalDate.now();
            return !parsedDate.isBefore(today);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validate if date and time are in the future
     */
    public static boolean isFutureDateTime(String date, String time) {
        if (date == null || date.isEmpty() || time == null || time.isEmpty()) {
            return false;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date, DATE_FORMATTER);
            LocalTime parsedTime = LocalTime.parse(time, TIME_FORMATTER);
            
            LocalDateTime appointmentDateTime = LocalDateTime.of(parsedDate, parsedTime);
            LocalDateTime currentDateTime = LocalDateTime.now();
            
            return appointmentDateTime.isAfter(currentDateTime);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validate time format (HH:mm)
     */
    public static boolean isValidTime(String time) {
        if (time == null || time.isEmpty()) {
            return false;
        }
        try {
            LocalTime.parse(time, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
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