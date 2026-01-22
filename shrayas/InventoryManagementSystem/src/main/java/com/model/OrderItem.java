package com.model;

public class OrderItem {

	private int orderItemId;
	private int orderId;
	private int productId;
	private int quantity;
	private double price;
	
	public OrderItem(int orderId, int productId, int quantity, double price) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}

	public OrderItem() {
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOrderItemId() {
		return orderItemId;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getProductId() {
		return productId;
	}

	public double getPrice() {
		return price;
	}

	public int getUserId() {
		return orderId;
	}

	public void setOrderId(int id) {
		this.orderId = id;
	}

	public void setProductId(int id) {
		this.productId = id;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setOrderItemId(int id) {
		this.orderItemId = id;
	}
	
}
