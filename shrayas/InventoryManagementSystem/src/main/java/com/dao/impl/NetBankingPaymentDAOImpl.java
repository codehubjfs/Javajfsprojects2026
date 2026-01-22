package com.dao.impl;

import java.sql.*;

import com.dao.NetBankingPaymentDAO;
import com.model.NetBankingPayment;
import com.util.DatabaseConnectionPool;

public class NetBankingPaymentDAOImpl implements NetBankingPaymentDAO{

	private static final String INSERT = """
	        INSERT INTO net_banking_payment (payment_id, bank_name, transaction_ref)
	        VALUES (?, ?, ?)
	        """;

	    private static final String FIND_BY_PAYMENT_ID = """
	        SELECT payment_id, bank_name, transaction_ref
	        FROM net_banking_payment
	        WHERE payment_id = ?
	        """;

	    @Override
	    public boolean save(NetBankingPayment payment) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, payment.getPaymentId());
	            ps.setString(2, payment.getBankName());
	            ps.setString(3, payment.getTransactionRef());

	            return ps.executeUpdate() > 0;
	        }
	    }

	    @Override
	    public NetBankingPayment findByPaymentId(int paymentId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_PAYMENT_ID)) {

	            ps.setInt(1, paymentId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToNetBankingPayment(rs);
	                }
	            }
	        }
	        return null;
	    }

	    private NetBankingPayment mapToNetBankingPayment(ResultSet rs) throws SQLException {

	        NetBankingPayment payment = new NetBankingPayment();
	        payment.setPaymentId(rs.getInt("payment_id"));
	        payment.setBankName(rs.getString("bank_name"));
	        payment.setTransactionRef(rs.getString("transaction_ref"));

	        return payment;
	    }
}
