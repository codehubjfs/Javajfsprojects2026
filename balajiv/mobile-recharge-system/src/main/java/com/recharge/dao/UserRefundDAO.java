package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.recharge.config.DBConnection;

public class UserRefundDAO {

	// query used to validate refund 
    private static final String VALIDATE_REFUND =
        """
        select
            rt.status as recharge_status,
            p.status as payment_status,
            p.payment_id,
            p.amount
        from payment p
        join recharge_transaction rt
            on p.recharge_id = rt.recharge_id
        where p.transaction_reference = ?
        """;
    
    /**
     * used to validate the refund requested by user
     * @param txnRef
     * @return
     */
    public RefundRequestData validateRefund(String txnRef) {

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(VALIDATE_REFUND);
            ps.setString(1, txnRef);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Invalid transaction reference");
            }
   
            return new RefundRequestData(
                rs.getString("recharge_status"),
                rs.getString("payment_status"),
                rs.getInt("payment_id"),
                rs.getDouble("amount")
            );

        } catch (Exception e) {
            throw new RuntimeException("Refund validation failed", e);
        }
    }

    // helper immutable refund data for requesting refund
    public static class RefundRequestData {
        public final String rechargeStatus;
        public final String paymentStatus;
        public final int paymentId;
        public final double amount;

        public RefundRequestData(String rechargeStatus, String paymentStatus, int paymentId, double amount) {
            this.rechargeStatus = rechargeStatus;
            this.paymentStatus = paymentStatus;
            this.paymentId = paymentId;
            this.amount = amount;
        }
    }
}
