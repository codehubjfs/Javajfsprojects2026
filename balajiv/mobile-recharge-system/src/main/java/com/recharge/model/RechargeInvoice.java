package com.recharge.model;

import java.time.LocalDateTime;

public class RechargeInvoice {
	
	private int invoiceId;
	private int rechargeId;
	private String invoiceUrl;
	private LocalDateTime generatedAt;
	
	public RechargeInvoice(int rechargeId, String invoiceUrl) {
		
		this.rechargeId = rechargeId;
		this.invoiceUrl = invoiceUrl;
	}
	
	public int getRechargeId() {
		return rechargeId;
	}
	
	public String getInvoiceUrl() {
		return invoiceUrl;
	}
	
}
