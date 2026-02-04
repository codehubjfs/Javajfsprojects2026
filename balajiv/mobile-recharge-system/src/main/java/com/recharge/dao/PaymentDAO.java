package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;
import com.recharge.model.Payment;

public class PaymentDAO {
	
	// used to get next attempt
	private static final String GET_NEXT_ATTEMPT = 
			"""
			select coalesce(max(attempt_number), 0) + 1
			from payment where recharge_id = ?
			""";
	
	// query used to insert payment 
	private static final String INSERT_PAYMENT = 
			"""
			insert into payment
			(recharge_id, payment_method, amount, status, transaction_reference, attempt_number, failure_reason, payment_time)
			values (?, ?, ?, ?, ?, ?, ?, now())
			""";
	
	// query used to get payment status
	private static final String GET_PAYMENT_STATUS = 
			"""
			select status from payment
			where recharge_id = ? 
			and attempt_number = (
				select max(attempt_number) 
				from payment where recharge_id = ?)
			""";
	
	/**
	 * used to get next attempt number using rechargeId
	 * @param rechargeId
	 * @return attemptNumber
	 */
	
	public int getNextAttemptNumber(int rechargeId) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_NEXT_ATTEMPT);
			ps.setInt(1,  rechargeId);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
			
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to get payment attempt number", e);
		}
	}
	
	/**
	 * used to record payment 
	 * @param payment
	 */
	
	public void recordPayment(Payment payment) {
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_PAYMENT);
			ps.setInt(1, payment.getRechargeId());
			ps.setString(2, payment.getPaymentMethod());
			ps.setDouble(3, payment.getAmount());
			ps.setString(4, payment.getStatus());
			ps.setString(5, payment.getTxnRef());
			ps.setInt(6, payment.getAttemptNumber());
			ps.setString(7, payment.getFailureReason());
			
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to record payment", e);
		}
	}
	
	/**
	 * used to get payment status 
	 * @param rechargeId
	 * @return status
	 */
	
	public String getPaymentStatus(int rechargeId) {
		
		try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(GET_PAYMENT_STATUS);
	        ps.setInt(1, rechargeId);
	        ps.setInt(2, rechargeId);

	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            throw new RuntimeException("No payment found for this recharge");
	        }

	        return rs.getString("status");

	    } 
		catch (Exception e) {
	        throw new RuntimeException("Failed to fetch payment status", e);
	    }
	}
	
	/**
	 * used to get payment amount
	 * @param paymentId
	 * @return amount
	 */
	
	public double getPaymentAmount(int paymentId) {

	    String sql = "SELECT amount FROM payment WHERE payment_id = ?";

	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, paymentId);

	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            throw new RuntimeException("Payment not found");
	        }

	        return rs.getDouble("amount");

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to fetch payment amount", e);
	    }
	}
	
	/**
	 * used to find successful payments
	 * @param txnRef
	 * @return
	 */
	
	public Payment findSuccessfulPaymentByTxnRef(String txnRef) {
		String latestSuccessfulPayment = """
				select payment_id, recharge_id, amount, status, transaction_reference
		        from payment
		        where transaction_reference = ?
		          and status = 'SUCCESS'
		        order by payment_time desc
		        limit 1
				""";
		
		try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(latestSuccessfulPayment);
	        ps.setString(1, txnRef);

	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            throw new RuntimeException("No successful payment found for this transaction reference");
	        }

	        Payment payment = new Payment();
	        payment.setPaymentId(rs.getInt("payment_id"));
	        payment.setRechargeId(rs.getInt("recharge_id"));
	        payment.setAmount(rs.getDouble("amount"));
	        payment.setStatus(rs.getString("status"));
	        payment.setTransactionReference(rs.getString("transaction_reference"));

	        return payment;

	    }
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch payment by transaction reference", e);
		}
	}
	
	/**
	 * used to get rechargeId by using transaction reference
	 * @param transactionRef
	 * @return rechargeId
	 */
	
	public int getRechargeIdByTransactionRef(String transactionRef) {
	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(
	            """
	            select recharge_id
	            from payment
	            where transaction_reference = ?
	            """
	        );
	        ps.setString(1, transactionRef);

	        ResultSet rs = ps.executeQuery();
	        if (!rs.next()) {
	            throw new RuntimeException("Invalid transaction reference");
	        }

	        return rs.getInt("recharge_id");

	    } catch (Exception e) {
	        throw new RuntimeException(
	            "Failed to resolve recharge from transaction reference", e
	        );
	    }
	}

	/**
	 * used to get payment status by transaction reference
	 * @param transactionRef
	 * @return
	 */
	
	public String getPaymentStatusByTxnRef(String transactionRef) {
	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(
	            """
	            select status
	            from payment
	            where transaction_reference = ?
	            order by attempt_number desc
	            limit 1
	            """
	        );
	        ps.setString(1, transactionRef);

	        ResultSet rs = ps.executeQuery();
	        if (!rs.next()) {
	            throw new RuntimeException("Payment not found for this transaction reference");
	        }

	        return rs.getString("status");

	    } catch (Exception e) {
	        throw new RuntimeException(
	            "Failed to fetch payment status", e
	        );
	    }
	}
	
	/**
	 * 	used to get all payments
	 * @return
	 */
	
	public List<String> findAllPayments() {

	    List<String> list = new ArrayList<>();

	    String sql =
	        "SELECT payment_id, recharge_id, attempt_number, amount, status " +
	        "FROM payment ORDER BY payment_id DESC";

	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            list.add(
	                rs.getInt("payment_id") + " | Recharge " +
	                rs.getInt("recharge_id") + " | Attempt " +
	                rs.getInt("attempt_number") + " | ₹" +
	                rs.getDouble("amount") + " | " +
	                rs.getString("status")
	            );
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to fetch payments", e);
	    }
	    return list;
	}
	
	/*
	 * used to final all successful paymentIds 
	 */
	public List<Integer> findSuccessfulPaymentIds() {

	    List<Integer> ids = new ArrayList<>();

	    String sql = "SELECT payment_id FROM payment WHERE status = 'SUCCESS'";

	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            ids.add(rs.getInt("payment_id"));
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to fetch successful payments", e);
	    }

	    return ids;
	}

}
