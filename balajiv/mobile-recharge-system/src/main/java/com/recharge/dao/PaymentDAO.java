package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.recharge.config.DBConnection;
import com.recharge.model.Payment;

public class PaymentDAO {
	
	private static final String GET_NEXT_ATTEMPT = 
			"""
			select coalesce(max(attempt_number), 0) + 1
			from payment where recharge_id = ?
			""";
	
	private static final String INSERT_PAYMENT = 
			"""
			insert into payment
			(recharge_id, payment_method, amount, status, attempt_number, failure_reason, payment_time)
			values (?, ?, ?, ?, ?, ?, now())
			""";
	
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
	
	public void recordPayment(Payment payment) {
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_PAYMENT);
			ps.setInt(1, payment.getRechargeId());
			ps.setString(2, payment.getPaymentMethod());
			ps.setDouble(3, payment.getAmount());
			ps.setString(4, payment.getStatus());
			ps.setInt(5, payment.getAttemptNumber());
			ps.setString(6, payment.getFailureReason());
			
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to record payment", e);
		}
	}
}
