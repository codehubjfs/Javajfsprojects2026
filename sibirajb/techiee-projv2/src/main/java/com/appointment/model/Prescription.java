package com.appointment.model;

public class Prescription {
    private int prescriptionId;
    private int appointmentId;
    private int doctorId;
    private int patientId;
    private String diagnosis;
    private String notes;

    public Prescription() {}

    public int getPrescriptionId() { 
    	return prescriptionId; 
    	}
    public void setPrescriptionId(int prescriptionId) { 
    	this.prescriptionId = prescriptionId;
    	}
    
    public int getAppointmentId() {
    	return appointmentId;
    	}
    public void setAppointmentId(int appointmentId) {
    	this.appointmentId = appointmentId; 
    	}
    
    public int getDoctorId() { 
    	return doctorId;
    }
    public void setDoctorId(int doctorId) { 
    	this.doctorId = doctorId;
    	}
    
    public int getPatientId() { 
    	return patientId; 
    	}
    public void setPatientId(int patientId) {
    	this.patientId = patientId; 
    	}
    
    public String getDiagnosis() { 
    	return diagnosis; 
    	}
    public void setDiagnosis(String diagnosis) {
    	this.diagnosis = diagnosis; 
    	}
    
    public String getNotes() { 
    	return notes; 
    	}
    public void setNotes(String notes) { 
    	this.notes = notes; 
    	}
}
