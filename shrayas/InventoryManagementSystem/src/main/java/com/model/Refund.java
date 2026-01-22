package com.model;

import java.time.LocalDateTime;

import com.enums.RefundStatus;

public class Refund {

	private int refundId;
	private int paymentId;
	private int orderId;
	private double refundAmount;
	private String refundReason;
	private RefundStatus refundStatus;
	private String gatewayRefundId;
	private LocalDateTime initiatedAt;
	private LocalDateTime completedAt;
	
	public Refund(int paymentId, int orderId, double refundAmount, String refundReason, RefundStatus refundStatus) {
		this.paymentId = paymentId;
		this.orderId = orderId;
		this.refundAmount = refundAmount;
		this.refundReason = refundReason;
		this.refundStatus = refundStatus;
	}

	
	public Refund() {
	}


	public void setRefundId(int refundId) {
		this.refundId = refundId;
	}


	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}


	public void setInitiatedAt(LocalDateTime initiatedAt) {
		this.initiatedAt = initiatedAt;
	}


	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}


	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public RefundStatus getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(RefundStatus refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getGatewayRefundId() {
		return gatewayRefundId;
	}

	public void setGatewayRefundId(String gatewayRefundId) {
		this.gatewayRefundId = gatewayRefundId;
	}

	public int getRefundId() {
		return refundId;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public int getOrderId() {
		return orderId;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public LocalDateTime getInitiatedAt() {
		return initiatedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}
	
	
}
