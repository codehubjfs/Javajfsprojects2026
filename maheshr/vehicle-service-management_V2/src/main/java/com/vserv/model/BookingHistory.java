package com.vserv.model;

import java.time.LocalDateTime;

/**
 * monitoring for booking modifications
 * 
 * @author Mahesh R
 */
public class BookingHistory {
    private int historyId;
    private int bookingId;
    private String actionType;
    private java.time.LocalDate oldServiceDate;
    private java.time.LocalDate newServiceDate;
    private String oldTimeSlot;
    private String newTimeSlot;
    private String reason;
    private int actionBy;
    private LocalDateTime actionDate;
    
    // For display purposes
    private String actionByName;
    
    public BookingHistory() {}
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] by %s on %s", 
            actionType, 
            actionByName != null ? actionByName : "User#" + actionBy,
            actionDate));
        
        if (oldServiceDate != null && newServiceDate != null) {
            sb.append(String.format("\n   From: %s at %s", oldServiceDate, oldTimeSlot));
            sb.append(String.format("\n   To: %s at %s", newServiceDate, newTimeSlot));
        }
        
        if (reason != null && !reason.isEmpty()) {
            sb.append("\n   Reason: ").append(reason);
        }
        
        return sb.toString();
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public java.time.LocalDate getOldServiceDate() {
        return oldServiceDate;
    }

    public void setOldServiceDate(java.time.LocalDate oldServiceDate) {
        this.oldServiceDate = oldServiceDate;
    }

    public java.time.LocalDate getNewServiceDate() {
        return newServiceDate;
    }

    public void setNewServiceDate(java.time.LocalDate newServiceDate) {
        this.newServiceDate = newServiceDate;
    }

    public String getOldTimeSlot() {
        return oldTimeSlot;
    }

    public void setOldTimeSlot(String oldTimeSlot) {
        this.oldTimeSlot = oldTimeSlot;
    }

    public String getNewTimeSlot() {
        return newTimeSlot;
    }

    public void setNewTimeSlot(String newTimeSlot) {
        this.newTimeSlot = newTimeSlot;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionByName() {
        return actionByName;
    }

    public void setActionByName(String actionByName) {
        this.actionByName = actionByName;
    }
}