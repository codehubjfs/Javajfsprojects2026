package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.CatalogDAO;
import com.vserv.model.Catalog;

public class CatalogDAOImpl implements CatalogDAO {

    @Override
    public List<Catalog> findAllServices() throws SQLException {
        List<Catalog> services = new ArrayList<>();
        String sql = """
                select * from service_catalog
                where is_active = TRUE ORDER BY service_type, service_name
                """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(mapResultSetToServiceCatalog(rs));
            }
        }
        return services;
    }

    @Override
    public List<Catalog> findByCarType(String carType) throws SQLException {
        List<Catalog> services = new ArrayList<>();
        String sql = """
                select * from service_catalog
                where is_active = TRUE AND car_type = ?
                ORDER BY service_type, service_name
                """;

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, carType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                services.add(mapResultSetToServiceCatalog(rs));
            }
        }
        return services;
    }

    @Override
    public Catalog findById(int catalogId) throws SQLException {
        String sql = """
                select * from service_catalog
                where catalog_id = ?
                """;

        try (Connection conn = DBConn.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, catalogId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToServiceCatalog(rs);
            }
        }
        return null;
    }

    private Catalog mapResultSetToServiceCatalog(ResultSet rs) throws SQLException {
        Catalog service = new Catalog();
        service.setCatalogId(rs.getInt("catalog_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setServiceType(rs.getString("service_type"));
        service.setDescription(rs.getString("description"));
        service.setBasePrice(rs.getBigDecimal("base_price"));
        service.setCarType(rs.getString("car_type"));
        service.setDurationHours(rs.getDouble("duration_hours"));
        service.setActive(rs.getBoolean("is_active"));
        return service;
    }
}