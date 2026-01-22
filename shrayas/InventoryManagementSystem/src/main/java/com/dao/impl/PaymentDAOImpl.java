package com.dao.impl;

import java.sql.*;

import com.dao.PaymentDAO;
import com.enums.PaymentStatus;
import com.enums.PaymentType;
import com.model.Payment;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;

public class PaymentDAOImpl implements PaymentDAO{

	private static final String INSERT = """
	        INSERT INTO payment 
	        (user_id, payment_transaction_id, payment_type, payment_status, transaction_amount)
	        VALUES (?, ?, ?, ?, ?)
	        """;

	    private static final String FIND_BY_ID = """
	        SELECT * FROM payment WHERE payment_id = ?
	        """;

	    private static final String FIND_BY_TRANSACTION_ID = """
	        SELECT * FROM payment WHERE payment_transaction_id = ?
	        """;

	    @Override
	    public int createPayment(Payment payment) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, payment.getUserId());
	            ps.setString(2, payment.getPaymentTransactionId());
	            ps.setString(3, payment.getPaymentType().name());
	            ps.setString(4, payment.getPaymentStatus().name());
	            ps.setDouble(5, payment.getTransactionAmount());

	            return ps.executeUpdate();
	        }

	    }

	    @Override
	    public Payment findById(int paymentId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_ID)) {

	            ps.setInt(1, paymentId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToPayment(rs);
	                }
	            }
	        }
	        return null;
	    }

	    @Override
	    public Payment findByTransactionId(String transactionId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_TRANSACTION_ID)) {

	            ps.setString(1, transactionId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToPayment(rs);
	                }
	            }
	        }
	        return null;
	    }

	    private Payment mapToPayment(ResultSet rs) throws SQLException {

	        Payment payment = new Payment();
	        payment.setPaymentId(rs.getInt("payment_id"));
	        payment.setUserId(rs.getInt("user_id"));
	        payment.setPaymentTransactionId(rs.getString("payment_transaction_id"));
	        payment.setPaymentType(PaymentType.valueOf(rs.getString("payment_type")));
	        payment.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));
	        payment.setTransactionAmount(rs.getDouble("transaction_amount"));
	        payment.setCreatedAt(DateUtil.toLocalDateTime(rs.getTimestamp("created_at")));

	        return payment;
	    }
}
