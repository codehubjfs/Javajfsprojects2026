package com.model;

import java.time.LocalDateTime;

public class Invoice {

	private int invoiceId;
	private int orderId;
	private int paymentId;
	private int userId;
	private LocalDateTime invoiceDate;
	private double totalAmount;
	
	public Invoice(int orderId, int paymentId, int userId, double totalAmount) {
		this.orderId = orderId;
		this.paymentId = paymentId;
		this.userId = userId;
		this.totalAmount = totalAmount;
	}
	
	public Invoice() {
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setInvoiceDate(LocalDateTime invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getInvoiceId() {
		return invoiceId;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public int getUserId() {
		return userId;
	}

	public LocalDateTime getInvoiceDate() {
		return invoiceDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceId=" + invoiceId + ", orderId=" + orderId + ", paymentId=" + paymentId + ", userId="
				+ userId + ", invoiceDate=" + invoiceDate + ", total_amount=" + totalAmount + "]";
	}
	
}
