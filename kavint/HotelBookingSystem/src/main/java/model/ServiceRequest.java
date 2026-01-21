package model;

public class ServiceRequest {

    private int requestId;
    private int bookingId;
    private int serviceId;
    private String requestStatus;
    private String requestedAt;

    public ServiceRequest(int requestId, int bookingId, int serviceId,
                          String requestStatus, String requestedAt) {
        this.requestId = requestId;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.requestStatus = requestStatus;
        this.requestedAt = requestedAt;
    }

    public int getRequestId() { 
    	return requestId; 
    }
    
    public void setRequestId(int requestId) { 
    	this.requestId = requestId; 
    }

    public int getBookingId() { 
    	return bookingId; 
    }
    
    public void setBookingId(int bookingId) { 
    	this.bookingId = bookingId; 
    }

    public int getServiceId() { 
    	return serviceId; 
    }
    
    public void setServiceId(int serviceId) { 
    	this.serviceId = serviceId; 
    }

    public String getRequestStatus() { 
    	return requestStatus; 
    }
    
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestedAt() { 
    	return requestedAt; 
    }
    
    public void setRequestedAt(String requestedAt) {
        this.requestedAt = requestedAt;
    }
}

