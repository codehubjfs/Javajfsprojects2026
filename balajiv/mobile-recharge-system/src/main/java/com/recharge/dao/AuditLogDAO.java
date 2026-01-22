package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.recharge.config.DBConnection;

public class AuditLogDAO {
	
	private static final String INSERT_AUDIT = 
			"""
			insert into audit_log
			(performed_by, entity_name, entity_id, action, old_value, new_value, timestamp)
			values(?, ?, ?, ?, ?, ?, now())
			""";
	
	public void log(int adminUserId, String entityName, int entityId, String action, String oldValue, String newValue) {
		try {
			Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_AUDIT);

            ps.setInt(1, adminUserId);
            ps.setString(2, entityName);
            ps.setInt(3, entityId);
            ps.setString(4, action);
            ps.setString(5, oldValue);
            ps.setString(6, newValue);

            ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to write log", e);
		}
	}
}
