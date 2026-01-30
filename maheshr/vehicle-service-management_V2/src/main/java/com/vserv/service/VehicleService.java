package com.vserv.service;

import java.sql.SQLException;
import java.util.List;

import com.vserv.dao.impl.VehicleDAOImpl;
import com.vserv.dao.interfaces.VehicleDAO;
import com.vserv.exception.BusinessLogicException;
import com.vserv.model.Vehicle;
import com.vserv.util.FieldValidator;

public class VehicleService {
	private VehicleDAO vehicleDAO;

	public VehicleService() {
        this.vehicleDAO = new VehicleDAOImpl();
	}

	public List<Vehicle> getVehiclesByUser(int userId) throws BusinessLogicException {
		try {
			return vehicleDAO.findByUserId(userId);
		} catch (SQLException e) {
			throw new BusinessLogicException(e.getMessage());
		}
	}

	public Vehicle getVehicleById(int vehicleId) throws BusinessLogicException {
		try {
			Vehicle vehicle = vehicleDAO.findById(vehicleId);
			if (vehicle == null) {
				throw new BusinessLogicException("Vehicle not found");
			}
			return vehicle;
		} catch (SQLException e) {
			throw new BusinessLogicException(e.getMessage());
		}
	}

	public Vehicle addVehicle(int userId, String carType, String brand, String model, String registrationNumber,
			int manufactureYear, int mileage) throws BusinessLogicException {
		try {
			if (!FieldValidator.isValidRegistrationNumber(registrationNumber)) {
				throw new BusinessLogicException("Invalid registration number format (e.g., TN01AB1234)");
			}

			if (!FieldValidator.isValidYear(manufactureYear)) {
				throw new BusinessLogicException("Invalid manufacture year");
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

		} catch (SQLException e) {
			if (e.getMessage().contains("Duplicate entry")) {
				throw new BusinessLogicException("Registration number already exists");
			}
			throw new BusinessLogicException(e.getMessage());
		}
	}

	public void updateVehicle(int vehicleId, int userId, String carType, String brand, String model,
			int manufactureYear, int mileage) throws BusinessLogicException {
		try {
			Vehicle vehicle = vehicleDAO.findById(vehicleId);
			if (vehicle == null) {
				throw new BusinessLogicException("Vehicle not found");
			}

			if (vehicle.getUserId() != userId) {
				throw new BusinessLogicException("Unauthorized: Vehicle does not belong to you");
			}
			if (!FieldValidator.isValidYear(manufactureYear)) {
				throw new BusinessLogicException("Invalid manufacture year");
			}
			vehicle.setCarType(carType);
			vehicle.setBrand(brand);
			vehicle.setModel(model);
			vehicle.setManufactureYear(manufactureYear);
			vehicle.setMileage(mileage);

			vehicleDAO.update(vehicle);

		} catch (SQLException e) {
			throw new BusinessLogicException("Error updating vehicle: " + e.getMessage());
		}
	}

	public void deleteVehicle(int vehicleId, int userId) throws BusinessLogicException {
		try {
			Vehicle vehicle = vehicleDAO.findById(vehicleId);
			if (vehicle == null) {
				throw new BusinessLogicException("Vehicle not found");
			}

			if (vehicle.getUserId() != userId) {
				throw new BusinessLogicException("Unauthorized: Vehicle does not belong to you");
			}

			vehicleDAO.delete(vehicleId);

		} catch (SQLException e) {
			if (e.getMessage().contains("foreign key constraint")) {
				throw new BusinessLogicException("Cannot delete vehicle with active bookings");
			}
			throw new BusinessLogicException("Error deleting vehicle: " + e.getMessage());
		}
	}

}
