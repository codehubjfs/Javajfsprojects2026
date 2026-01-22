package com.model;

import com.enums.Status;

public class Category {

	private int categoryId;
	private String categoryName;
	private String description;
	private Status status;
	
	public Category(String categoryName, String description) {
		this.categoryName = categoryName;
		this.description = description;
	}
	
	public Category() {
	}

	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int id) {
		this.categoryId = id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryName=" + categoryName + ", description="
				+ description + "]";
	}

	
}
