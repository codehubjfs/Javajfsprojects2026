package com.model;

import java.time.LocalDateTime;

import com.enums.StockTransactionType;

public class StockTransaction {

	private int stockTransactionId;
	private int productId;
	private int userId;
	private StockTransactionType type;
	private int quantity;
	private LocalDateTime createdAt;
	
	public StockTransaction(int productId, int userId, StockTransactionType type, int quantity) {
		this.productId = productId;
		this.userId = userId;
		this.type = type;
		this.quantity = quantity;
	}

	public void setStockTransactionId(int stockTransactionId) {
		this.stockTransactionId = stockTransactionId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public StockTransaction() {
	}

	public int getProductId() {
		return productId;
	}

	public int getUserId() {
		return userId;
	}

	public StockTransactionType getType() {
		return type;
	}

	public void setType(StockTransactionType type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getStockTransactionId() {
		return stockTransactionId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
}
