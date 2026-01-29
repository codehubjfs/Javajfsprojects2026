package com.appointment.model;

import java.sql.Date;
import java.sql.Time;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String status;
    private String symptoms;
    private Doctor doctor;
    private Patient patient;

    public Appointment() {}

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
    public int getDoctorId() { 
    	return doctorId; 
    	}
    public void setDoctorId(int doctorId) {
    	this.doctorId = doctorId;
    	}  
    public Date getAppointmentDate() {
    	return appointmentDate; 
    	}
    public void setAppointmentDate(Date appointmentDate) { 
    	this.appointmentDate = appointmentDate; 
    	}   
    public Time getAppointmentTime() { 
    	return appointmentTime;
    	}
    public void setAppointmentTime(Time appointmentTime) {
    	this.appointmentTime = appointmentTime;
    	}   
    public String getStatus() { 
    	return status;
    }
    public void setStatus(String status) { 
    	this.status = status;
    	}    
    public String getSymptoms() {
    	return symptoms; 
    }
    public void setSymptoms(String symptoms) { 
    	this.symptoms = symptoms; 
    	}  
    public Doctor getDoctor() { 
    	return doctor; 
    	}
    public void setDoctor(Doctor doctor) { 
    	this.doctor = doctor; 
    	}
    public Patient getPatient() { 
    	return patient; 
    	}
    public void setPatient(Patient patient) {
    	this.patient = patient; 
    	}
}
