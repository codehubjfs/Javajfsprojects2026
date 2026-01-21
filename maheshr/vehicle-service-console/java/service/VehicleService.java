package service;

import dao.VehicleDAO;
import exception.BusinessException;
import model.Vehicle;
import util.ValidationUtil;
import java.sql.SQLException;
import java.util.List;

public class VehicleService {
    private VehicleDAO vehicleDAO;
    
    public VehicleService() {
        this.vehicleDAO = new VehicleDAO();
    }
    
    public List<Vehicle> getVehiclesByUser(int userId) throws BusinessException {
        try {
            return vehicleDAO.findByUserId(userId);
        } 
        catch (SQLException e) {
            throw new BusinessException(e.getMessage());
        }
    }
    
    public Vehicle getVehicleById(int vehicleId) throws BusinessException {
        try {
            Vehicle vehicle = vehicleDAO.findById(vehicleId);
            if (vehicle == null) {
                throw new BusinessException("Vehicle not found");
            }
            return vehicle;
        } 
        catch (SQLException e) {
            throw new BusinessException(e.getMessage());
        }
    }
    
    public Vehicle addVehicle(int userId, String carType, String brand, String model,
                              String registrationNumber, int manufactureYear, int mileage) 
            throws BusinessException {
        try {
            if (!ValidationUtil.isValidRegistrationNumber(registrationNumber)) {
                throw new BusinessException("Invalid registration number format (e.g., TN01AB1234)");
            }
            
            if (!ValidationUtil.isValidYear(manufactureYear)) {
                throw new BusinessException("Invalid manufacture year");
            }
            
            Vehicle vehicle = new Vehicle();
            vehicle.setUserId(userId);
            vehicle.setCarType(carType);
            vehicle.setBrand(brand);
            vehicle.setModel(model);
            vehicle.setRegistrationNumber(registrationNumber.toUpperCase());
            vehicle.setManufactureYear(manufactureYear);
            vehicle.setMileage(mileage);
            vehicle.setServiceIntervalKm(10000);
            
            return vehicleDAO.insert(vehicle);
            
        } 
        catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new BusinessException("Registration number already exists");
            }
            throw new BusinessException(e.getMessage());
        }
    }
    
}
