package com.vserv.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Advisor {
	private int advisorId;
	private String fullName;
	private String email;
	private String specialization;
	private BigDecimal overtimeRate;
	private String availabilityStatus;
	private int currentLoad;
	private LocalDateTime lastAssignedAt;

	public int getAdvisorId() {
		return advisorId;
	}

	public void setAdvisorId(int advisorId) {
		this.advisorId = advisorId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public BigDecimal getOvertimeRate() {
		return overtimeRate;
	}

	public void setOvertimeRate(BigDecimal overtimeRate) {
		this.overtimeRate = overtimeRate;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public int getCurrentLoad() {
		return currentLoad;
	}

	public void setCurrentLoad(int currentLoad) {
		this.currentLoad = currentLoad;
	}

	public LocalDateTime getLastAssignedAt() {
		return lastAssignedAt;
	}

	public void setLastAssignedAt(LocalDateTime lastAssignedAt) {
		this.lastAssignedAt = lastAssignedAt;
	}

	@Override
	public String toString() {
		return String.format("%s - %s (Load: %d)", fullName, specialization, currentLoad);
	}
}