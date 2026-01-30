package com.vserv.model;

import java.math.BigDecimal;

public class WorkItem {
	private int workItemId;
	private String itemName;
	private String itemType;
	private BigDecimal unitPrice;
	private String carType;
	private String description;
	private boolean isActive;

	public int getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(int workItemId) {
		this.workItemId = workItemId;
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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return String.format("%s - Rs. %.2f [%s]", itemName, unitPrice, itemType);
	}
}