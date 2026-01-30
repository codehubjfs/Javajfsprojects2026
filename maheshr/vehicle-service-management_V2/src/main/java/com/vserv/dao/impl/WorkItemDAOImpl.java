package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.WorkItemDAO;
import com.vserv.model.WorkItem;

public class WorkItemDAOImpl implements WorkItemDAO {

    @Override
    public List<WorkItem> findAll() throws SQLException {
        List<WorkItem> items = new ArrayList<>();
        String sql = """
            SELECT * FROM work_item_catalog 
            WHERE is_active = TRUE 
            ORDER BY item_type, item_name
            """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(mapResultSet(rs));
            }
        }
        return items;
    }

    @Override
    public List<WorkItem> findByCarType(String carType) throws SQLException {
        List<WorkItem> items = new ArrayList<>();
        String sql = """
            SELECT * FROM work_item_catalog 
            WHERE is_active = TRUE AND (car_type = ? OR car_type = 'ALL')
            ORDER BY item_type, item_name
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, carType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(mapResultSet(rs));
            }
        }
        return items;
    }

    @Override
    public WorkItem findById(int workItemId) throws SQLException {
        String sql = "SELECT * FROM work_item_catalog WHERE work_item_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, workItemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    private WorkItem mapResultSet(ResultSet rs) throws SQLException {
        WorkItem item = new WorkItem();
        item.setWorkItemId(rs.getInt("work_item_id"));
        item.setItemName(rs.getString("item_name"));
        item.setItemType(rs.getString("item_type"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setCarType(rs.getString("car_type"));
        item.setDescription(rs.getString("description"));
        item.setActive(rs.getBoolean("is_active"));
        return item;
    }
}