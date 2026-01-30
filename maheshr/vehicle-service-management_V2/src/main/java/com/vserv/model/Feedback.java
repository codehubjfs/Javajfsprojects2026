package com.vserv.model;

import java.time.LocalDateTime;

public class Feedback {
	private int feedbackId;
	private int serviceId;
	private int customerId;
	private int rating;
	private String feedbackText;
	private LocalDateTime submittedAt;

	// For display
	private String customerName;
	private String vehicleInfo;

	public int getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getVehicleInfo() {
		return vehicleInfo;
	}

	public void setVehicleInfo(String vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}

	public String getStarRating() {
		return "*".repeat(rating) + "-".repeat(5 - rating);
	}

	@Override
	public String toString() {
		return String.format("%s - %s: %s", vehicleInfo, getStarRating(), feedbackText);
	}
}