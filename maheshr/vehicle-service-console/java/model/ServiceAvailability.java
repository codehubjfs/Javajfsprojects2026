package model;

import java.time.LocalDate;

public class ServiceAvailability {
    private int availabilityId;
    private LocalDate serviceDate;
    private String timeSlot;
    private int maxBookings;
    private int currentBookings;
    private boolean isAvailable;
    
    public ServiceAvailability() {}
    
    
    public int getAvailabilityId() {
		return availabilityId;
	}


	public void setAvailabilityId(int availabilityId) {
		this.availabilityId = availabilityId;
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


	public int getMaxBookings() {
		return maxBookings;
	}


	public void setMaxBookings(int maxBookings) {
		this.maxBookings = maxBookings;
	}


	public int getCurrentBookings() {
		return currentBookings;
	}


	public void setCurrentBookings(int currentBookings) {
		this.currentBookings = currentBookings;
	}


	public boolean isAvailable() {
		return isAvailable;
	}


	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}


	public int getSlotsRemaining() {
        return maxBookings - currentBookings;
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | Slots: %d/%d", 
            serviceDate, timeSlot, getSlotsRemaining(), maxBookings);
    }
}