package com.vserv.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice {
	private int invoiceId;
	private int serviceId;
	private BigDecimal itemsTotal;
	private BigDecimal overtimeCharge;
	
	// for precision preserving
	private BigDecimal totalAmount;
	private LocalDate invoiceDate;
	private String paymentStatus;

	// for display
	private String vehicleInfo;
	private String customerName;
	private String serviceName;

	public int getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getVehicleInfo() {
		return vehicleInfo;
	}

	public void setVehicleInfo(String vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public BigDecimal getItemsTotal() {
	    return itemsTotal;
	}

	public void setItemsTotal(BigDecimal itemsTotal) {
	    this.itemsTotal = itemsTotal;
	}

	public BigDecimal getOvertimeCharge() {
	    return overtimeCharge;
	}
	
	public void setOvertimeCharge(BigDecimal overtimeCharge) {
	    this.overtimeCharge = overtimeCharge;
	}

	@Override
	public String toString() {
		return String.format("Invoice #%d - %s - Rs. %.2f [%s]", invoiceId, vehicleInfo, totalAmount, paymentStatus);
	}
}