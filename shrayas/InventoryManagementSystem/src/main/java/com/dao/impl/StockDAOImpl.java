package com.dao.impl;

import java.sql.*;

import com.dao.StockDAO;
import com.model.Stock;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;

public class StockDAOImpl implements StockDAO{

	private static final String INSERT = """
	        INSERT INTO stock (product_id, quantity, min_threshold)
	        VALUES (?, ?, ?)
	        """;

	    private static final String FIND_BY_PRODUCT_ID = """
	        SELECT stock_id, product_id, quantity, min_threshold, created_at
	        FROM stock
	        WHERE product_id = ?
	        """;

	    private static final String UPDATE_QUANTITY = """
	        UPDATE stock
	        SET quantity = ?
	        WHERE product_id = ?
	        """;

	    private static final String UPDATE_THRESHOLD = """
	        UPDATE stock
	        SET min_threshold = ?
	        WHERE product_id = ?
	        """;

	    @Override
	    public int createStock(int productId, int quantity, int minThreshold) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, productId);
	            ps.setInt(2, quantity);
	            ps.setInt(3, minThreshold);

	            return ps.executeUpdate();
	        }

	    }

	    @Override
	    public Stock findByProductId(int productId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_PRODUCT_ID)) {

	            ps.setInt(1, productId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToStock(rs);
	                }
	            }
	        }
	        return null;
	    }

	    @Override
	    public boolean updateQuantity(int productId, int quantity) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(UPDATE_QUANTITY)) {

	            ps.setInt(1, quantity);
	            ps.setInt(2, productId);

	            return ps.executeUpdate() > 0;
	        }
	    }

	    @Override
	    public boolean updateThreshold(int productId, int minThreshold) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(UPDATE_THRESHOLD)) {

	            ps.setInt(1, minThreshold);
	            ps.setInt(2, productId);

	            return ps.executeUpdate() > 0;
	        }
	    }

	    private Stock mapToStock(ResultSet rs) throws SQLException {

	        Stock stock = new Stock();
	        stock.setStockId(rs.getInt("stock_id"));
	        stock.setProductId(rs.getInt("product_id"));
	        stock.setQuantity(rs.getInt("quantity"));
	        stock.setMinThreshold(rs.getInt("min_threshold"));
	        stock.setCreatedAt(DateUtil.toLocalDateTime(rs.getTimestamp("created_at")));

	        return stock;
	    }
}
