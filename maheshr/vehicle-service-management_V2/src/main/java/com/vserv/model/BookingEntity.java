package com.vserv.model;

import java.time.LocalDate;

/**
 * Abstract base class for booking entities
 * 
 * @author Mahesh R
 */
public abstract class BookingEntity {
    protected LocalDate serviceDate;
    protected String timeSlot;
    
    /**
     * Abstract method subclasses must implement
     */
    public abstract String getDisplayInfo();
    
    /**
     * Concrete method shared logic
     */
    public boolean isUpcoming() {
        return serviceDate != null && 
               (serviceDate.isAfter(LocalDate.now()) || 
                serviceDate.equals(LocalDate.now()));
    }
    
    public LocalDate getServiceDate() {
        return serviceDate;
    }
    
    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }
    
    public String getTimeSlot() {
        return timeSlot;
    }
    
    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}