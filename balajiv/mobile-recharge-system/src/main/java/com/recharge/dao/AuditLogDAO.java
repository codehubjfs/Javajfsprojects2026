package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;


import com.recharge.config.DBConnection;


public class AuditLogDAO {
	
	// query used to insert logs.
	private static final String INSERT_AUDIT = 
			"""
			insert into audit_log
			(performed_by, entity_name, entity_id, action, old_value, new_value, timestamp)
			values(?, ?, ?, ?, ?, ?, now())
			""";
	
	/**
	 * Used to add the log in the db
	 * @param adminUserId
	 * @param entityName
	 * @param entityId
	 * @param action
	 * @param oldValue
	 * @param newValue
	 */
	
	public void log(int adminUserId, String entityName, Integer entityId, String action, String oldValue, String newValue) {
		
		try {
			Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_AUDIT);

            ps.setInt(1, adminUserId);
            ps.setString(2, entityName);
            
            // entity_id can be NULL
            if (entityId == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, entityId);
            }

            ps.setString(4, action);
   
            if (oldValue == null) {
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(5, oldValue);
            }

            if (newValue == null) {
                ps.setNull(6, Types.VARCHAR);
            } else {
                ps.setString(6, newValue);
            }

            ps.executeUpdate();

        } 
		catch (Exception e) {
            throw new RuntimeException("Failed to write audit log", e);
        }
    }
}
