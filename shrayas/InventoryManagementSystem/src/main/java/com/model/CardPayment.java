package com.model;

public class CardPayment {

	private int paymentId;
	private String gatewayName;
	private String gatewayTransactionId;
	private String authorizationCode;
	
	public CardPayment(int paymentId, String gatewayName) {
		this.paymentId = paymentId;
		this.gatewayName = gatewayName;
	}
	
	public CardPayment() {
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public String getGatewayName() {
		return gatewayName;
	}
	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}
	public String getGatewayTransactionId() {
		return gatewayTransactionId;
	}
	public void setGatewayTransactionId(String gatewayTransactionId) {
		this.gatewayTransactionId = gatewayTransactionId;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public int getPaymentId() {
		return paymentId;
	}
	
}
