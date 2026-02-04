package com.recharge.model;

import java.time.LocalDateTime;

public class Payment {

	private int paymentId;
	private int rechargeId;
	private String paymentMethod;
	private double amount;
	private String status;
	private String transactionRef;
	private int attemptNumber;
	private String failureReason;
	private LocalDateTime paymentTime;
	

	public Payment(int rechargeId, String paymentMethod, double amount, String status, String transactionRef, 
			int attemptNumber, String failureReason) {
		this.rechargeId = rechargeId;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.status = status;
		this.transactionRef = transactionRef;
		this.attemptNumber = attemptNumber;
		this.failureReason = failureReason;
	}

	public Payment() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Getter functions
	 */
	public int getPaymentId() {
		return paymentId;
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
    
    public String getTxnRef() {
    	return transactionRef;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public String getFailureReason() {
        return failureReason;
    }
    
    public LocalDateTime getPaymentTime() {
    	return paymentTime;
    }
    
    /*
     * Setter Functions
     */
    public void setPaymentId(int id) {
    	paymentId = id;
    }
    
    public void setRechargeId(int id) {
    	rechargeId = id;
    }
    
    public void setAmount(double amount) {
    	this.amount = amount;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    
    public void setTransactionReference(String txnRef) {
    	transactionRef = txnRef;
    }
    
}
