package model;

public class Service {

    private int serviceId;
    private String serviceName;
    private double serviceCost;

    public Service(int serviceId, String serviceName, double serviceCost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public int getServiceId() { 
    	return serviceId; 
    }
    
    public void setServiceId(int serviceId) { 
    	this.serviceId = serviceId; 
    }

    public String getServiceName() { 
    	return serviceName; 
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getServiceCost() { 
    	return serviceCost; 
    }
    
    public void setServiceCost(double serviceCost) {
        this.serviceCost = serviceCost;
    }
}

