package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dao.UPIPaymentDAO;
import com.model.UPIPayment;
import com.util.DatabaseConnectionPool;

public class UPIPaymentDAOImpl implements UPIPaymentDAO{

	private static final String INSERT = """
	        INSERT INTO upi_payment
	        (payment_id, upi_id, app_name, gateway_transaction_id)
	        VALUES (?, ?, ?, ?)
	        """;

	    private static final String FIND_BY_PAYMENT_ID = """
	        SELECT payment_id, upi_id, app_name, gateway_transaction_id
	        FROM upi_payment
	        WHERE payment_id = ?
	        """;

	    @Override
	    public boolean save(UPIPayment payment) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, payment.getPaymentId());
	            ps.setString(2, payment.getUpiId());
	            ps.setString(3, payment.getAppName().name());
	            ps.setString(4, payment.getGatewayTransactionId());

	            return ps.executeUpdate() > 0;
	        }
	    }

	    @Override
	    public UPIPayment findByPaymentId(int paymentId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_PAYMENT_ID)) {

	            ps.setInt(1, paymentId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToUPIPayment(rs);
	                }
	            }
	        }
	        return null;
	    }

	    private UPIPayment mapToUPIPayment(ResultSet rs) throws SQLException {

	        UPIPayment payment = new UPIPayment();
	        payment.setPaymentId(rs.getInt("payment_id"));
	        payment.setUpiId(rs.getString("upi_id"));
	        payment.setAppName(PaymentAppName.valueOf(rs.getString("app_name")));
	        payment.setGatewayTransactionId(rs.getString("gateway_transaction_id"));

	        return payment;
	    }
}
