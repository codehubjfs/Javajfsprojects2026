package com.vserv.model;

import java.math.BigDecimal;

public class ServiceItem {
	private int itemId;
	private int serviceId;
	private int workItemId;
	private int quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;

	// For display
	private String itemName;
	private String itemType;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(int workItemId) {
		this.workItemId = workItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	@Override
	public String toString() {
		return String.format("%s x%d @ Rs. %.2f = Rs. %.2f", itemName, quantity, unitPrice, totalPrice);
	}
}