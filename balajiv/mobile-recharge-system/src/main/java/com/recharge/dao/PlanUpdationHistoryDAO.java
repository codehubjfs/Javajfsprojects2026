package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.recharge.config.DBConnection;

public class PlanUpdationHistoryDAO {

	// insert plan updation history to the db
	private static final String INSERT_HISTORY = 
			"""
			insert into plan_updation_history
			(plan_id, old_price, new_price, changed_by)
			values(?, ?, ?, ?, now())
			""";
	
	public void recordPriceChange(int planId, double oldPrice, double newPrice, int adminUserId) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_HISTORY);
			
			ps.setInt(1, planId);
			ps.setDouble(2, oldPrice);
			ps.setDouble(3, newPrice);
			ps.setInt(4, adminUserId);
			
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to record the plan updation history", e);
		}
	}
	
}
