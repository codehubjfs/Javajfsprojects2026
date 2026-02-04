package com.recharge.model;

import java.time.LocalDate;

public class Offer {

	private String title;
    private String discountType;   
    private double discountValue;
    private LocalDate startDate;
    private LocalDate endDate;

    public Offer(String title, String discountType, double discountValue, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
	 * getter functions
     */
	public String getTitle() {
        return title;
    }

    public String getDiscountType() {
        return discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
