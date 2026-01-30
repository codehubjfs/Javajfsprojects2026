package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.ItemDAO;
import com.vserv.model.ServiceItem;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public List<ServiceItem> findByServiceId(int serviceId) throws SQLException {
        List<ServiceItem> items = new ArrayList<>();
        String sql = """
            select si.*, wic.item_name, wic.item_type
            from service_item si
            JOIN work_item_catalog wic ON si.work_item_id = wic.work_item_id
            where si.service_id = ?
            ORDER BY si.item_id
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                items.add(mapResultSet(rs));
            }
        }
        return items;
    }

    @Override
    public ServiceItem insert(ServiceItem item) throws SQLException {
        String sql = """
            INSERT INTO service_item (service_id, work_item_id, quantity, unit_price)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, item.getServiceId());
            stmt.setInt(2, item.getWorkItemId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getUnitPrice());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                item.setItemId(rs.getInt(1));
            }
        }
        return item;
    }

    @Override
    public void delete(int itemId) throws SQLException {
        String sql = "DELETE from service_item where item_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        }
    }

    private ServiceItem mapResultSet(ResultSet rs) throws SQLException {
        ServiceItem item = new ServiceItem();
        item.setItemId(rs.getInt("item_id"));
        item.setServiceId(rs.getInt("service_id"));
        item.setWorkItemId(rs.getInt("work_item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setTotalPrice(rs.getBigDecimal("total_price"));
        item.setItemName(rs.getString("item_name"));
        item.setItemType(rs.getString("item_type"));
        return item;
    }
}