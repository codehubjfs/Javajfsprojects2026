package com.model;

import java.time.LocalDateTime;

public class Stock {

	private int stockId;
	private int productId;
	private int quantity;
	private int minThreshold;
	private LocalDateTime createdAt;
	
	public Stock(int productId, int quantity, int minThreshold) {
		this.productId = productId;
		this.quantity = quantity;
		this.minThreshold = minThreshold;
	}
	
	public Stock() {
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getMinThreshold() {
		return minThreshold;
	}

	public void setMinThreshold(int minThreshold) {
		this.minThreshold = minThreshold;
	}

	public int getStockId() {
		return stockId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
}
