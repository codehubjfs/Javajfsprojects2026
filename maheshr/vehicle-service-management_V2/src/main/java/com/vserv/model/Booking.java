package com.vserv.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking extends BookingEntity implements Comparable<Booking> {
    private int bookingId;
    private int vehicleId;
    private int catalogId;

    private String bookingStatus;
    private String bookingNotes;
    private LocalDateTime createdAt;
    
    private String vehicleInfo;
    private String serviceName;
    
    // Service Record fields
    private Integer serviceRecordId;
    private String serviceStatus;
    private String serviceRemarks;
    private String advisorName;
    private LocalDateTime serviceStartDate;
    private LocalDateTime serviceEndDate;
    
    public Booking() {}
    
    /**
     * Implement abstract method from BookingEntity
     */
    @Override
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s - %s on %s at %s", 
            bookingStatus, vehicleInfo, serviceName, serviceDate, timeSlot));
        
        if (hasServiceRecord()) {
            sb.append(String.format(" | Service: %s", serviceStatus));
            if (advisorName != null) {
                sb.append(String.format(" (Advisor: %s)", advisorName));
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Check if this booking has an assigned service record
     */
    public boolean hasServiceRecord() {
        return serviceRecordId != null;
    }
    
    /**
     * Check if booking can be assigned to advisor
     */
    public boolean isAssignable() {
        return ("PENDING".equals(bookingStatus) || 
                "CONFIRMED".equals(bookingStatus) || 
                "RESCHEDULED".equals(bookingStatus)) &&
               !hasServiceRecord();
    }
    
    /**
     * comparable most recent bookings first
     */
    @Override
    public int compareTo(Booking other) {
        // Recent date
        int dateCompare = other.serviceDate.compareTo(this.serviceDate);
        if (dateCompare != 0) {
            return dateCompare;
        }
        // Recent id
        return Integer.compare(other.bookingId, this.bookingId);
    }

    // Existing getters and setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
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

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingNotes() {
        return bookingNotes;
    }

    public void setBookingNotes(String bookingNotes) {
        this.bookingNotes = bookingNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getServiceRecordId() {
        return serviceRecordId;
    }

    public void setServiceRecordId(Integer serviceRecordId) {
        this.serviceRecordId = serviceRecordId;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceRemarks() {
        return serviceRemarks;
    }

    public void setServiceRemarks(String serviceRemarks) {
        this.serviceRemarks = serviceRemarks;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

    public LocalDateTime getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(LocalDateTime serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDateTime getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(LocalDateTime serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
    }
}