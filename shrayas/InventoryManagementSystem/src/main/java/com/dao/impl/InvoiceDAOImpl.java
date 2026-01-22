package com.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.model.Invoice;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;

public class InvoiceDAOImpl {

	private static final String INSERT = """
	        INSERT INTO invoice (order_id, user_id, payment_id, total_amount)
	        VALUES (?, ?, ?, ?)
	        """;

	    private static final String FIND_BY_ID = """
	        SELECT * FROM invoice WHERE invoice_id = ?
	        """;

	    private static final String FIND_BY_ORDER_ID = """
	        SELECT * FROM invoice WHERE order_id = ?
	        """;

	    private static final String FIND_BY_USER_ID = """
	        SELECT * FROM invoice WHERE user_id = ?
	        """;

	    public int generateInvoice(Invoice invoice) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, invoice.getOrderId());
	            ps.setInt(2, invoice.getUserId());
	            ps.setInt(3, invoice.getPaymentId());
	            ps.setDouble(4, invoice.getTotalAmount());

	            return ps.executeUpdate();
	        }

	    }

	    public Invoice findById(int invoiceId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_ID)) {

	            ps.setInt(1, invoiceId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToInvoice(rs);
	                }
	            }
	        }
	        return null;
	    }

	    public Invoice findByOrderId(int orderId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_ORDER_ID)) {

	            ps.setInt(1, orderId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToInvoice(rs);
	                }
	            }
	        }
	        return null;
	    }

	    public List<Invoice> findByUserId(int userId) throws Exception {

	        List<Invoice> invoices = new ArrayList<>();

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_USER_ID)) {

	            ps.setInt(1, userId);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    invoices.add(mapToInvoice(rs));
	                }
	            }
	        }
	        return invoices;
	    }

	    private Invoice mapToInvoice(ResultSet rs) throws SQLException {

	        Invoice invoice = new Invoice();
	        invoice.setInvoiceId(rs.getInt("invoice_id"));
	        invoice.setOrderId(rs.getInt("order_id"));
	        invoice.setUserId(rs.getInt("payment_id"));
	        invoice.setTotalAmount(rs.getDouble("total_amount"));
	        invoice.setInvoiceDate(DateUtil.toLocalDateTime(rs.getTimestamp("created_at")));

	        return invoice;
	    }
    
}
