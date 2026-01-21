package model;

import java.math.BigDecimal;

public class ServiceCatalog {
    private int catalogId;
    private String serviceName;
    private String serviceType;
    private String description;
    private BigDecimal basePrice;
    private String carType;
    private double durationHours;
    private boolean isActive;
    
    public ServiceCatalog() {}
    
    
    public int getCatalogId() {
		return catalogId;
	}


	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public String getServiceType() {
		return serviceType;
	}


	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public BigDecimal getBasePrice() {
		return basePrice;
	}


	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}


	public String getCarType() {
		return carType;
	}


	public void setCarType(String carType) {
		this.carType = carType;
	}


	public double getDurationHours() {
		return durationHours;
	}


	public void setDurationHours(double durationHours) {
		this.durationHours = durationHours;
	}


	public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	@Override
    public String toString() {
        return String.format("[%d] %s - ₹%.2f (%s, %.1fh)", 
            catalogId, serviceName, basePrice, carType, durationHours);
    }
}