package com.recharge.model;


public class RechargePlan {
    
	private int operatorId;
	private String planName;
	private double price;
	private int validityDays;
	private String dataBenefits;
	private String callBenefits;
	private String smsBenefits;
	private String planType;
	
	public RechargePlan(int operatorId, String planName, double price, int validityDays, String dataBenefits, String callBenefits, String smsBenefits,  String planType) {
		this.operatorId = operatorId;
		this.planName = planName;
		this.price = price;
		this.validityDays = validityDays;
		this.dataBenefits = dataBenefits;
		this.callBenefits = callBenefits;
		this.smsBenefits = smsBenefits;
		this.planType = planType;
	}
	
	/*
	 * Getter Functions
	 */
	public int getOperatorId() {
        return operatorId;
    }

    public String getPlanName() {
        return planName;
    }

    public double getPrice() {
        return price;
    }

    public int getValidityDays() {
        return validityDays;
    }
    
    public String getDataBenefits() {
    		return dataBenefits;
    }
    
    public String getCallBenefits() {
    		return callBenefits;
    }
    
    public String getSmsBenefits() {
    		return smsBenefits;
    }
    
    public String getPlanType() {
        return planType;
    }
}
