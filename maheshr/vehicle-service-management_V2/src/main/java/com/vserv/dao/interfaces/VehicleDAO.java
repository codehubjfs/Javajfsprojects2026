package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

import com.vserv.model.Vehicle;

public interface VehicleDAO {
    List<Vehicle> findByUserId(int userId) throws SQLException;
    List<Vehicle> findAll() throws SQLException;
    Vehicle findById(int vehicleId) throws SQLException;
    Vehicle insert(Vehicle vehicle) throws SQLException;
    void update(Vehicle vehicle) throws SQLException;
    void delete(int vehicleId) throws SQLException;
    int getTotalCount() throws SQLException;
}