package com.recharge.model;

public class Refund {

    private int paymentId;
    private double amount;
    private String status;   

    public Refund(int paymentId, double amount, String status) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.status = status;
    }

	/*
	 * Getter Functions
	 */
    
    public int getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
