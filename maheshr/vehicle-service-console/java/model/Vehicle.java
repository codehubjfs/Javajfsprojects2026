package model;

import java.time.LocalDate;

public class Vehicle {
    private int vehicleId;
    private int userId;
    private String carType;
    private String brand;
    private String model;
    private String registrationNumber;
    private int manufactureYear;
    private int mileage;
    private LocalDate lastServiceDate;
    private LocalDate nextServiceDue;
    private int serviceIntervalKm;
    
    public Vehicle() {}
    
    public int getVehicleId() {
		return vehicleId;
	}


	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getCarType() {
		return carType;
	}


	public void setCarType(String carType) {
		this.carType = carType;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getRegistrationNumber() {
		return registrationNumber;
	}


	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}


	public int getManufactureYear() {
		return manufactureYear;
	}


	public void setManufactureYear(int manufactureYear) {
		this.manufactureYear = manufactureYear;
	}


	public int getMileage() {
		return mileage;
	}


	public void setMileage(int mileage) {
		this.mileage = mileage;
	}


	public LocalDate getLastServiceDate() {
		return lastServiceDate;
	}


	public void setLastServiceDate(LocalDate lastServiceDate) {
		this.lastServiceDate = lastServiceDate;
	}


	public LocalDate getNextServiceDue() {
		return nextServiceDue;
	}


	public void setNextServiceDue(LocalDate nextServiceDue) {
		this.nextServiceDue = nextServiceDue;
	}


	public int getServiceIntervalKm() {
		return serviceIntervalKm;
	}


	public void setServiceIntervalKm(int serviceIntervalKm) {
		this.serviceIntervalKm = serviceIntervalKm;
	}


	@Override
    public String toString() {
        return String.format("%s %s %s (%s) - Mileage: %d km", 
            brand, model, carType, registrationNumber, mileage);
    }
}