package com.model;

import com.enums.PaymentAppName;

public class UPIPayment {

	private int paymentId;
	private String upiId;
	private PaymentAppName appName;
	private String gatewayTransactionId;
	
	public UPIPayment(String upiId, PaymentAppName appName) {
		this.upiId = upiId;
		this.appName = appName;
	}
	
	public UPIPayment() {
	}

	public String getGatewayTransactionId() {
		return gatewayTransactionId;
	}

	public void setGatewayTransactionId(String gatewayTransactionId) {
		this.gatewayTransactionId = gatewayTransactionId;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public String getUpiId() {
		return upiId;
	}

	public PaymentAppName getAppName() {
		return appName;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public void setAppName(PaymentAppName appName) {
		this.appName = appName;
	}
	
}
