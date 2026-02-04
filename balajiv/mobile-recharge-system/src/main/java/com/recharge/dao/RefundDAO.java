package com.recharge.dao;

import com.recharge.config.DBConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RefundDAO {

	// query used to insert the refund 
    private static final String INSERT_REFUND =
        """
        INSERT INTO refund
        (payment_id, amount, status, processed_at)
        VALUES (?, ?, 'INITIATED', NOW())
        """;

    // query used to update the refund status
    private static final String UPDATE_STATUS =
        "UPDATE refund SET status = 'PROCESSED', processed_at = NOW() WHERE refund_id = ?";

    // query used to check whether refund exists
    private static final String REFUND_COUNT = "select count(*) from refund where payment_id = ?";
    
    /**
     * used to check refund exists
     * @param paymentId
     * @return 
     */
    public boolean refundExists(int paymentId) {
    	try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(REFUND_COUNT);
            ps.setInt(1, paymentId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;

        } catch (Exception e) {
            throw new RuntimeException("Failed to check refund existence", e);
        }
    }
    
    /**
     * used to create the refund 
     * @param paymentId
     * @param amount
     * @return refundId
     */
    public int createRefund(int paymentId, double amount) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_REFUND, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setInt(1, paymentId);
            ps.setDouble(2, amount);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create refund", e);
        }
    }
    
    /**
     * used to get refundId by transaction ref
     * @param txnRef
     * @return
     */
    public int getRefundIdByTransactionRef(String txnRef) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                """
                select r.refund_id
                from refund r
                join payment p on r.payment_id = p.payment_id
                where p.transaction_reference = ?
                  and r.status = 'INITIATED'
                """
            );
            ps.setString(1, txnRef);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("No initiated refund found for this transaction reference");
            }

            return rs.getInt("refund_id");

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve refund", e);
        }
    }

    /**
     * used to complete the refund
     * @param refundId
     */
    public void completeRefund(int refundId) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS);
           
            ps.setInt(1, refundId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to complete refund", e);
        }
    }
}
