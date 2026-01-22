package com.model;

import com.enums.DeliveryStatus;

public class CODPayment {

	private int paymentId;
	private double amount;
	private DeliveryStatus deliveryStatus;
	
	public CODPayment(int paymentId, double amount, DeliveryStatus deliveryStatus) {
		this.paymentId = paymentId;
		this.amount = amount;
		this.deliveryStatus = deliveryStatus;
	}

	public CODPayment() {
	}

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public double getAmount() {
		return amount;
	}

	public void setPaymentId(int id) {
		this.paymentId = id;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
