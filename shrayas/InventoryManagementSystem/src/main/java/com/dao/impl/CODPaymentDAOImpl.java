package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dao.CODPaymentDAO;
import com.enums.DeliveryStatus;
import com.model.CODPayment;
import com.util.DatabaseConnectionPool;

public class CODPaymentDAOImpl implements CODPaymentDAO{
	
	private static final String INSERT = """
	        INSERT INTO cod_payment (payment_id, amount, delivery_status)
	        VALUES (?, ?, ?)
	        """;

	    private static final String FIND_BY_PAYMENT_ID = """
	        SELECT payment_id, amount, delivery_status
	        FROM cod_payment
	        WHERE payment_id = ?
	        """;

	    private static final String UPDATE_DELIVERY_STATUS = """
	        UPDATE cod_payment
	        SET delivery_status = ?
	        WHERE payment_id = ?
	        """;

	    @Override
	    public boolean save(CODPayment payment) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, payment.getPaymentId());
	            ps.setDouble(2, payment.getAmount());
	            ps.setString(3, payment.getDeliveryStatus().name());

	            return ps.executeUpdate() > 0;
	        }
	    }

	    @Override
	    public CODPayment findByPaymentId(int paymentId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_PAYMENT_ID)) {

	            ps.setInt(1, paymentId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToCODPayment(rs);
	                }
	            }
	        }
	        return null;
	    }

	    @Override
	    public boolean updateDeliveryStatus(int paymentId, DeliveryStatus status) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(UPDATE_DELIVERY_STATUS)) {

	            ps.setString(1, status.name());
	            ps.setInt(2, paymentId);

	            return ps.executeUpdate() > 0;
	        }
	    }

	    private CODPayment mapToCODPayment(ResultSet rs) throws SQLException {

	        CODPayment payment = new CODPayment();
	        payment.setPaymentId(rs.getInt("payment_id"));
	        payment.setAmount(rs.getDouble("amount"));
	        payment.setDeliveryStatus(
	                DeliveryStatus.valueOf(rs.getString("delivery_status")));

	        return payment;
	    }

}
