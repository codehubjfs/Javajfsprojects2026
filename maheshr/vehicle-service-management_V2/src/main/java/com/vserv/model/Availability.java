package com.vserv.model;

public class Availability extends BookingEntity {
    private int availabilityId;

    private int maxBookings;
    private int currentBookings;
    private boolean isAvailable;
    
public Availability() {}
    
    /**
     * Implement abstract method from BookingEntity
     */
    @Override
    public String getDisplayInfo() {
        return String.format("%s | %s | Slots: %d/%d %s", 
            serviceDate, timeSlot, getSlotsRemaining(), maxBookings,
            isAvailable ? "" : "[CLOSED]");
    }
    
    public int getSlotsRemaining() {
        return maxBookings - currentBookings;
    }
    
    @Override
    public String toString() {
        return getDisplayInfo();
    }    
    
    public int getAvailabilityId() {
		return availabilityId;
	}


	public void setAvailabilityId(int availabilityId) {
		this.availabilityId = availabilityId;
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

}