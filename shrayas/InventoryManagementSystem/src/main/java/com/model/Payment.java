package com.model;

import java.time.LocalDateTime;

import com.enums.PaymentStatus;
import com.enums.PaymentType;

public class Payment {

	private int paymentId;
	private int userId;
	private String paymentTransactionId;
	private PaymentType paymentType;
	private PaymentStatus paymentStatus;
	private double transactionAmount;
	private LocalDateTime createdAt;
	
	public Payment(int userId, PaymentType paymentType, PaymentStatus paymentStatus, double transactionAmount) {
		this.userId = userId;
		this.paymentType = paymentType;
		this.paymentStatus = paymentStatus;
		this.transactionAmount = transactionAmount;
	}
	
	public Payment() {
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getUserId() {
		return userId;
	}
	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}
	public void setPaymentTransactionId(String paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public double getTransactionAmount() {
		return transactionAmount;
	}
	public int getPaymentId() {
		return paymentId;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
}
