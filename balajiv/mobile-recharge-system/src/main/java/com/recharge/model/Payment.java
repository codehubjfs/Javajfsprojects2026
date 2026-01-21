package com.recharge.model;

import java.time.LocalDateTime;

public class Payment {

	private int paymentId;
	private int rechargeId;
	private String paymentMethod;
	private double amount;
	private String status;
	private int attemptNumber;
	private String failureReason;
	private LocalDateTime paymentTime;
	
	public Payment(int rechargeId, String paymentMethod, double amount, String status,
			int attemptNumber, String failureReason) {
		this.rechargeId = rechargeId;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.status = status;
		this.attemptNumber = attemptNumber;
		this.failureReason = failureReason;
	}
	
	public int getRechargeId() {
        return rechargeId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
