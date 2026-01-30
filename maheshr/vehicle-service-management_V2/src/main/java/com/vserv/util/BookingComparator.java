package com.vserv.util;

import java.time.LocalDate;
import java.util.Comparator;

import com.vserv.model.Booking;

/**
 * Custom comparators for booking sorting
 * 
 * @author Mahesh R
 */
public class BookingComparator {
    
    /**
     * Sort by date: earlier dates first
     */
    public static final Comparator<Booking> BY_DATE = (b1, b2) -> {
        LocalDate date1 = b1.getServiceDate();
        LocalDate date2 = b2.getServiceDate();
        
        if (date1.isBefore(date2)) {
            return -1;
        } else if (date1.isAfter(date2)) {
            return 1;
        } else {
            return 0;
        }
    };
    
    /**
     * Sort by status priority: pending > confirmed > others
     */
    public static final Comparator<Booking> BY_STATUS = (b1, b2) -> {
        int p1 = getStatusPriority(b1.getBookingStatus());
        int p2 = getStatusPriority(b2.getBookingStatus());
        return Integer.compare(p1, p2);
    };
    
    private static int getStatusPriority(String status) {
        return switch (status) {
            case "PENDING" -> 1;
            case "CONFIRMED" -> 2;
            case "RESCHEDULED" -> 3;
            case "COMPLETED" -> 4;
            case "CANCELLED" -> 5;
            default -> 6;
        };
    }
}