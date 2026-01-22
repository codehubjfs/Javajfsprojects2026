package com.model;

public class NetBankingPayment {

	private int paymentId;
	private String bankName;
	private String transactionRef;
	
	public NetBankingPayment(int paymentId, String bankName) {
		this.paymentId = paymentId;
		this.bankName = bankName;
	}

	public NetBankingPayment() {
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int id) {
		this.paymentId = id;
	}
	
}
