package com.model;

import java.sql.Date;
import java.time.LocalDateTime;

import com.enums.OrderStatus;
import com.enums.PaymentStatus;

public class Order {

	private int orderId;
	private int userId;
	private double totalAmount;
	private OrderStatus orderStatus;
	private PaymentStatus paymentStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Order(int userId, double totalAmount) {
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.orderStatus = OrderStatus.CREATED;
		this.paymentStatus = PaymentStatus.CREATED;
	}

	public Order() {
	}

	public int getUserId() {
		return userId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getOrderId() {
		return orderId;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setOrderId(int id) {
		this.orderId = id;
	}
	
	public void setUserId(int id) {
		this.userId = id;
	}

	public void setCreatedAt(LocalDateTime date) {
		this.createdAt = date;
	}
	
	public void setUpdatedAt(LocalDateTime date) {
		this.updatedAt = date;
	}
}
