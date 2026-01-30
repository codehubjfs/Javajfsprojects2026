package com.vserv.model;

import java.time.LocalDateTime;

public class ServiceRecord {
	private int serviceId;
	private int bookingId;
	private int advisorId;
	private LocalDateTime serviceStartDate;
	private LocalDateTime serviceEndDate;
	private String status;
	private String remarks;
	private Double estimatedHours;
	private Double actualHours;

	// For display purposes
	private String advisorName;
	private String vehicleInfo;
	private String serviceName;

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAdvisorName() {
		return advisorName;
	}

	public void setAdvisorName(String advisorName) {
		this.advisorName = advisorName;
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
	
	public Double getEstimatedHours() {
	    return estimatedHours;
	}

	public void setEstimatedHours(Double estimatedHours) {
	    this.estimatedHours = estimatedHours;
	}

	public Double getActualHours() {
	    return actualHours;
	}

	public void setActualHours(Double actualHours) {
	    this.actualHours = actualHours;
	}
	
	public double getOvertimeHours() {
	    if (actualHours == null || estimatedHours == null) {
	        return 0.0;
	    }
	    return Math.max(0, actualHours - estimatedHours);
	}

	@Override
	public String toString() {
		return String.format("[%s] %s - %s", status, vehicleInfo, serviceName);
	}
}