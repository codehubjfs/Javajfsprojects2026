package com.vserv.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * 
 * @author Mahesh R
 */
public class InputValidator {
    private static Scanner reader = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /** 
     * @param prompt Display text for user
     * @return Trimmed string
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = reader.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }
    
    /**
     * 
     * @param prompt Display text for user
     * @return Valid integer
     */
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = reader.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Read date in yyyy-MM-dd format
     * 
     * @param prompt Display text for user
     * @return Valid LocalDate
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (yyyy-mm-dd): ");
                String input = reader.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Date cannot be empty. Please try again.");
                    continue;
                }
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use yyyy-mm-dd (e.g., 2026-01-22)");
            }
        }
    }
    
    /**
     * 
     * @param prompt Display text for user
     * @return Valid double value
     */
    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = reader.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a number.");
                    continue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
    
    public static void load() {
        System.out.println("\nPress Enter to continue...");
        reader.nextLine();
    }
    
    public static void close() {
        reader.close();
    }
    
    private InputValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}