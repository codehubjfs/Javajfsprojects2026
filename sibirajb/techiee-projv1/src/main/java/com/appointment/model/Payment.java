package com.appointment.model;

public class Payment {
    private int paymentId;
    private int appointmentId;
    private int patientId;
    private double amount;
    private String paymentStatus;

    public Payment() {}

    public int getPaymentId() { 
    	return paymentId; 
    	}
    public void setPaymentId(int paymentId) { 
    	this.paymentId = paymentId; 
    	}
    
    public int getAppointmentId() { 
    	return appointmentId; 
    	}
    public void setAppointmentId(int appointmentId) {
    	this.appointmentId = appointmentId;
    	}
    
    public int getPatientId() { 
    	return patientId; 
    	}
    public void setPatientId(int patientId) {
    	this.patientId = patientId; 
    	}
    
    public double getAmount() { 
    	return amount; 
    	}
    public void setAmount(double amount) { 
    	this.amount = amount;
    	}
    
    public String getPaymentStatus() {
    	return paymentStatus; 
    	}
    public void setPaymentStatus(String paymentStatus) { 
    	this.paymentStatus = paymentStatus;
    	}
}

