package com.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dao.CardPaymentDAO;
import com.model.CardPayment;
import com.util.DatabaseConnectionPool;

public class CardPaymentDAOImpl implements CardPaymentDAO{

	private static final String INSERT = """
	        INSERT INTO card_payment
	        (payment_id, gateway_name, gateway_transaction_id, authorization_code)
	        VALUES (?, ?, ?, ?)
	        """;

	    private static final String FIND_BY_PAYMENT_ID = """
	        SELECT payment_id, gateway_name, gateway_transaction_id, authorization_code
	        FROM card_payment
	        WHERE payment_id = ?
	        """;

	    @Override
	    public boolean save(CardPayment cardPayment) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, cardPayment.getPaymentId());
	            ps.setString(2, cardPayment.getGatewayName());
	            ps.setString(3, cardPayment.getGatewayTransactionId());
	            ps.setString(4, cardPayment.getAuthorizationCode());

	            return ps.executeUpdate() > 0;
	        }
	    }

	    @Override
	    public CardPayment findByPaymentId(int paymentId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_PAYMENT_ID)) {

	            ps.setInt(1, paymentId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToCardPayment(rs);
	                }
	            }
	        }
	        return null;
	    }

	    private CardPayment mapToCardPayment(ResultSet rs) throws SQLException {

	        CardPayment cardPayment = new CardPayment();
	        cardPayment.setPaymentId(rs.getInt("payment_id"));
	        cardPayment.setGatewayName(rs.getString("gateway_name"));
	        cardPayment.setGatewayTransactionId(rs.getString("gateway_transaction_id"));
	        cardPayment.setAuthorizationCode(rs.getString("authorization_code"));

	        return cardPayment;
	    }
}
