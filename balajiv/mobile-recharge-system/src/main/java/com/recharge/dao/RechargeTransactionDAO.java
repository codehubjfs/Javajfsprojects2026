package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;


public class RechargeTransactionDAO {
	
	// query used to insert recharge transaction
	private static final String INSERT_TX = 
			"""
            insert into recharge_transaction
            (user_id, connection_id, plan_id, final_amount, status, initiated_at)
            values (?, ?, ?, ?, 'INITIATED', now())
            """;
	
	/**
	 * used to create recharge
	 * @param userId
	 * @param connectionId
	 * @param planId
	 * @param amount
	 * @return
	 */
	
	public int createRecharge(int userId, int connectionId, int planId, double amount) {
		try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(INSERT_TX, PreparedStatement.RETURN_GENERATED_KEYS);

	        ps.setInt(1, userId);
	        ps.setInt(2, connectionId);
	        ps.setInt(3, planId);
	        ps.setDouble(4, amount);

	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();
	        rs.next();
	        return rs.getInt(1);

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to create recharge transaction", e);
	    }
	}
	
	/**
	 * used to get the recharge status
	 * @param rechargeId
	 * @return
	 */
	public String getStatus(int rechargeId) {

	    String sql =
	        "SELECT status FROM recharge_transaction WHERE recharge_id = ?";

	    try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, rechargeId);

	        ResultSet rs = ps.executeQuery();

	        if (!rs.next()) {
	            throw new RuntimeException("Recharge not found");
	        }

	        return rs.getString("status");

	    } catch (Exception e) {
	        throw new RuntimeException("Failed to fetch recharge status", e);
	    }
	}

	/**
	 * used to get connectionId by rechargeId
	 * @param rechargeId
	 * @return
	 */
	
	public int getConnectionId(int rechargeId) {
		try {
			Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(
	                "select connection_id from recharge_transaction where recharge_id = ?"
	        );
	        ps.setInt(1, rechargeId);

	        ResultSet rs = ps.executeQuery();
	        rs.next();
	        return rs.getInt("connection_id");
		}
		catch(Exception e) {
			 throw new RuntimeException("Failed to fetch connection id", e);
		}
	}
	
	/**
	 * used to update recharge status
	 * @param rechargeId
	 * @param status
	 */
	
	public void updateStatus(int rechargeId, String status) {
		
		try {
			Connection conn = DBConnection.getConnection();
			
			String sql;
			if("PAYMENT_IN_PROGRESS".equals(status)) {
				sql = "update recharge_transaction set status = ?, completed_at = null where recharge_id = ?";
			}
			else {
				sql = "update recharge_transaction set status = ?, completed_at = now() where recharge_id = ?";
			}
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1,  status);
			ps.setInt(2,  rechargeId);
			
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Error updating recharge status", e);
		}
		
	}
	
	/**
	 * used to get all recharge transactions
	 * @return
	 */
	public List<String> findAllTransactions(){
		
		List<String> list = new ArrayList<>();
		
		String sql = """
					select recharge_id, user_id, final_amount, status, initiated_at, completed_at
					from recharge_transaction order by recharge_id desc
					 """;
		
		try {
	        Connection conn = DBConnection.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        
	        while(rs.next()) {
	        	list.add(rs.getInt("recharge_id") + " | User " + rs.getInt("user_id")+" | ₹" + rs.getDouble("final_amount")+" | "+
	        			rs.getString("status")+" | " + rs.getTimestamp("initiated_at")+" | "+rs.getTimestamp("completed_at"));
	        }
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch recharge transactions", e);
		}
		return list;
	}
}
