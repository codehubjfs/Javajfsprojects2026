package com.ecommerce.models;

import java.sql.Timestamp;

public class Payment {
	private int paymentId;
	private int orderId;
	private String method;
	private String status; 
	private String transactionId; 
	private Timestamp paymentDate; 
	
	
	public Payment(int paymentId, int orderId, String method, String status, String transactionId, Timestamp paymentDate) { 
		this.paymentId = paymentId; 
		this.orderId = orderId; 
		this.method = method; 
		this.status = status; 
		this.transactionId = transactionId; 
		this.paymentDate = paymentDate; 
	}
	
	public int getPaymentId() {
		return paymentId;
	}
	public int getOrderId() {
		return orderId;
	}
	public String getMethod() {
		return method;
	}
	public String getStatus() {
		return status;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public Timestamp getPaymentDate() {
		return paymentDate;
	}
}
