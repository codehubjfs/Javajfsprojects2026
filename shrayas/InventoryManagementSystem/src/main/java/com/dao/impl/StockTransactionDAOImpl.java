package com.dao.impl;

import java.sql.*;
import java.util.*;

import com.dao.StockTransactionDAO;
import com.enums.StockTransactionType;
import com.model.StockTransaction;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;

public class StockTransactionDAOImpl implements StockTransactionDAO{

	private static final String INSERT = """
	        INSERT INTO stock_transaction
	        (product_id, user_id, type, quantity)
	        VALUES (?, ?, ?, ?)
	        """;

	    private static final String FIND_BY_PRODUCT_ID = """
	        SELECT stock_transaction_id, product_id, user_id, type, quantity, created_at
	        FROM stock_transaction
	        WHERE product_id = ?
	        ORDER BY created_at DESC
	        """;

	    private static final String FIND_BY_USER_ID = """
	        SELECT stock_transaction_id, product_id, user_id, type, quantity, created_at
	        FROM stock_transaction
	        WHERE user_id = ?
	        ORDER BY created_at DESC
	        """;

	    @Override
	    public int save(StockTransaction transaction) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, transaction.getProductId());
	            ps.setInt(2, transaction.getUserId());
	            ps.setString(3, transaction.getType().name());
	            ps.setInt(4, transaction.getQuantity());

	            return ps.executeUpdate();

	    }

	    @Override
	    public List<StockTransaction> findByProductId(int productId) throws Exception {

	        List<StockTransaction> transactions = new ArrayList<>();

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_PRODUCT_ID)) {

	            ps.setInt(1, productId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    transactions.add(mapToStockTransaction(rs));
	                }
	            }
	        }
	        return transactions;
	    }

	    @Override
	    public List<StockTransaction> findByUserId(int userId) throws Exception {

	        List<StockTransaction> transactions = new ArrayList<>();

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_USER_ID)) {

	            ps.setInt(1, userId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    transactions.add(mapToStockTransaction(rs));
	                }
	            }
	        }
	        return transactions;
	    }

	    private StockTransaction mapToStockTransaction(ResultSet rs) throws SQLException {

	        StockTransaction transaction = new StockTransaction();
	        transaction.setStockTransactionId(rs.getInt("stock_transaction_id"));
	        transaction.setProductId(rs.getInt("product_id"));
	        transaction.setUserId(rs.getInt("user_id"));
	        transaction.setType(StockTransactionType.valueOf(rs.getString("type")));
	        transaction.setQuantity(rs.getInt("quantity"));
	        transaction.setCreatedAt(DateUtil.toLocalDateTime(rs.getTimestamp("created_at")));

	        return transaction;
	    }
}
