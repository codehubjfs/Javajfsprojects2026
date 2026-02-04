package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;

public class ConnectionDAO {
	
	// query used to get mobile number.
	private static final String GET_MOBILE_NUM = 
			"""
			select mobile_number from mobile_connection
			where connection_id = ?
			""";
	
	// query used to check the connection exists
	private static final String CONNECTION_EXISTS = 
			"""
			select count(*) from mobile_connection 
			where mobile_number = ?
			""";
	
	// query uesd to check the operator
	private static final String GET_OPERATOR_ID = 
			"""
			select operator_id from operator 
			where operator_name = ? and status = 'ACTIVE'
			""";
	
	// query used to insert the mobile connection
	private static final String INSERT_MOBILE_CONNECTION =  
			"""
            insert into mobile_connection
            (mobile_number, operator_id, circle, status)
            values (?, ?, ?, 'ACTIVE')
            """;
	
	// query used to get all available connections
	private static final String GET_ALL_CONNECTIONS = 
			"""
            select distinct mc.mobile_number, o.operator_name
            from recharge_transaction rt
            join mobile_connection mc
                on rt.connection_id = mc.connection_id
            join operator o
                on mc.operator_id = o.operator_id
            where rt.user_id = ?
            """;
	
	// query used to get the connection Id
	private static final String GET_CONNECTION_ID = 
			"""
			select connection_id from mobile_connection 
			where mobile_number = ?
			""";
	
	/**
	 * check mobile number exists or not
	 * @param mobile
	 * @return boolean
	 */
	
	public boolean mobileExists(String mobile) {
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(CONNECTION_EXISTS);
			
			ps.setString(1, mobile);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1) > 0;
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to check mobile number", e);
		}
	}

	/**
	 * used to get the operator Id by operator name
	 * @param operatorName
	 * @return operatorId
	 */
	
	public int getOperatorIdByName(String operatorName) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_OPERATOR_ID);
            ps.setString(1, operatorName);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Operator not found or inactive");
            }
            return rs.getInt("operator_id");
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve operator", e);
        }
    }

	/**
	 * used to create the connection
	 * @param mobile
	 * @param operatorId
	 * @param circle
	 * @return connectionId
	 */
	
	public int createConnection(String mobile, int operatorId, String circle) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_MOBILE_CONNECTION, PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, mobile);
            ps.setInt(2, operatorId);
            ps.setString(3, circle);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to add mobile number", e);
        }
    }
	
	/**
	 * used to get mobile number
	 * @param connectionId
	 * @return mobile_number
	 */
	
    public String getMobileNumber(int connectionId) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_MOBILE_NUM);
            ps.setInt(1, connectionId);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getString("mobile_number");

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch mobile number", e);
        }
    }
    
    /**
     * used to get connectionId by mobile number
     * @param mobile
     * @return connectionId
     */
    
    public int getConnectionId(String mobile) {
    	try {
    		Connection conn = DBConnection.getConnection();
    		PreparedStatement ps = conn.prepareStatement(GET_CONNECTION_ID);
    		
    		ps.setString(1, mobile);
    		
    		ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Mobile number not found");
            }
            return rs.getInt("connection_id");
    	}
    	catch(Exception e) {
    		throw new RuntimeException("Failed to resolve mobile number", e);
    	}
    }
    
    /**
     * used to get all available connections.
     * @param userId
     * @return
     */
    
    public List<String> getUserConnections(int userId) {
        List<String> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_ALL_CONNECTIONS);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(
                    rs.getString("mobile_number") + " | " +
                    rs.getString("operator_name"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch connections", e);
        }
        return list;
    }
}
