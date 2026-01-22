package com.dao.impl;

import java.sql.*;
import java.time.LocalDateTime;

import com.dao.RefundDAO;
import com.enums.RefundStatus;
import com.model.Refund;
import com.util.DatabaseConnectionPool;
import com.util.DateUtil;

public class RefundDAOImpl implements RefundDAO{

	private static final String INSERT = """
	        INSERT INTO refund
	        (payment_id, order_id, refund_amount, refund_reason, refund_status)
	        VALUES (?, ?, ?, ?, ?)
	        """;

	    private static final String FIND_BY_ID = """
	        SELECT * FROM refund WHERE refund_id = ?
	        """;

	    private static final String FIND_BY_PAYMENT_ID = """
	        SELECT * FROM refund WHERE payment_id = ?
	        """;

	    private static final String FIND_BY_ORDER_ID = """
	        SELECT * FROM refund WHERE order_id = ?
	        """;

	    private static final String UPDATE_STATUS = """
	        UPDATE refund
	        SET refund_status = ?, gateway_refund_id = ?, completed_at = ?
	        WHERE refund_id = ?
	        """;

	    @Override
	    public int createRefund(Refund refund) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(INSERT)) {

	            ps.setInt(1, refund.getPaymentId());
	            ps.setInt(2, refund.getOrderId());
	            ps.setDouble(3, refund.getRefundAmount());
	            ps.setString(4, refund.getRefundReason());
	            ps.setString(5, refund.getRefundStatus().name());

	            return ps.executeUpdate();
	        }
	    }

	    @Override
	    public Refund findById(int refundId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_ID)) {

	            ps.setInt(1, refundId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToRefund(rs);
	                }
	            }
	        }
	        return null;
	    }

	    @Override
	    public Refund findByPaymentId(int paymentId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_PAYMENT_ID)) {

	            ps.setInt(1, paymentId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToRefund(rs);
	                }
	            }
	        }
	        return null;
	    }

	    @Override
	    public Refund findByOrderId(int orderId) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(FIND_BY_ORDER_ID)) {

	            ps.setInt(1, orderId);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return mapToRefund(rs);
	                }
	            }
	        }
	        return null;
	    }

	    @Override
	    public boolean updateRefundStatus(
	            int refundId,
	            RefundStatus status,
	            String gatewayRefundId,
	            LocalDateTime completedAt) throws Exception {

	        try (Connection con = DatabaseConnectionPool.getConnection();
	             PreparedStatement ps = con.prepareStatement(UPDATE_STATUS)) {

	            ps.setString(1, status.name());
	            ps.setString(2, gatewayRefundId);
	            ps.setTimestamp(3,
	                completedAt != null ? Timestamp.valueOf(completedAt) : null);
	            ps.setInt(4, refundId);

	            return ps.executeUpdate() > 0;
	        }
	    }

	    private Refund mapToRefund(ResultSet rs) throws SQLException {

	        Refund refund = new Refund();
	        refund.setRefundId(rs.getInt("refund_id"));
	        refund.setPaymentId(rs.getInt("payment_id"));
	        refund.setOrderId(rs.getInt("order_id"));
	        refund.setRefundAmount(rs.getDouble("refund_amount"));
	        refund.setRefundReason(rs.getString("refund_reason"));
	        refund.setRefundStatus(RefundStatus.valueOf(rs.getString("refund_status")));
	        refund.setGatewayRefundId(rs.getString("gateway_refund_id"));
	        refund.setInitiatedAt(DateUtil.toLocalDateTime(rs.getTimestamp("initiated_at")));
	        refund.setCompletedAt(DateUtil.toLocalDateTime(rs.getTimestamp("completed_at")));

	        return refund;
	    }
}
