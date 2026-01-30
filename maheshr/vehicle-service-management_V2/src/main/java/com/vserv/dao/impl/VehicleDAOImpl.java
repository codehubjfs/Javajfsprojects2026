package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.VehicleDAO;
import com.vserv.model.Vehicle;

public class VehicleDAOImpl implements VehicleDAO {

    @Override
    public List<Vehicle> findByUserId(int userId) throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = """
                SELECT * FROM vehicle
                WHERE user_id = ? ORDER BY vehicle_id""";

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findAll() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = """
                SELECT * FROM vehicle
                ORDER BY vehicle_id
                """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        }
        return vehicles;
    }

    @Override
    public Vehicle findById(int vehicleId) throws SQLException {
        String sql = """
                SELECT * FROM vehicle
                WHERE vehicle_id = ?""";

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVehicle(rs);
            }
        }
        return null;
    }

    @Override
    public Vehicle insert(Vehicle vehicle) throws SQLException {
        String sql = """
                INSERT INTO vehicle (user_id, car_type, brand, model,
                    registration_number, manufacture_year, mileage,
                    service_interval_km) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, vehicle.getUserId());
            stmt.setString(2, vehicle.getCarType());
            stmt.setString(3, vehicle.getBrand());
            stmt.setString(4, vehicle.getModel());
            stmt.setString(5, vehicle.getRegistrationNumber());
            stmt.setInt(6, vehicle.getManufactureYear());
            stmt.setInt(7, vehicle.getMileage());
            stmt.setInt(8, vehicle.getServiceIntervalKm());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                vehicle.setVehicleId(rs.getInt(1));
            }
        }
        return vehicle;
    }
    
    @Override
    public void update(Vehicle vehicle) throws SQLException {
        String sql = "UPDATE vehicle SET car_type = ?, brand = ?, model = ?, "
                + "manufacture_year = ?, mileage = ? WHERE vehicle_id = ?";

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getCarType());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setInt(4, vehicle.getManufactureYear());
            stmt.setInt(5, vehicle.getMileage());
            stmt.setInt(6, vehicle.getVehicleId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int vehicleId) throws SQLException {
        String sql = "DELETE FROM vehicle WHERE vehicle_id = ?";

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            stmt.executeUpdate();
        }
    }

    @Override
    public int getTotalCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM vehicle";

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId(rs.getInt("vehicle_id"));
        vehicle.setUserId(rs.getInt("user_id"));
        vehicle.setCarType(rs.getString("car_type"));
        vehicle.setBrand(rs.getString("brand"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setRegistrationNumber(rs.getString("registration_number"));
        vehicle.setManufactureYear(rs.getInt("manufacture_year"));
        vehicle.setMileage(rs.getInt("mileage"));
        vehicle.setServiceIntervalKm(rs.getInt("service_interval_km"));

        Date lastServiceDate = rs.getDate("last_service_date");
        if (lastServiceDate != null) {
            vehicle.setLastServiceDate(lastServiceDate.toLocalDate());
        }

        Date nextServiceDue = rs.getDate("next_service_due");
        if (nextServiceDue != null) {
            vehicle.setNextServiceDue(nextServiceDue.toLocalDate());
        }

        return vehicle;
    }
}