package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;

public class OperatorDAO {
	
	// getting all available operators from db
	private static final String GET_ALL = "select operator_id, operator_name, status from operator";
	
	// it checks if the selected operator is exits in the table.
	private static final String CHECK_EXISTS = "select count(*) from operator where operator_name = ?";
	
	// it inserts new operator 
	private static final String INSERT_OPERATOR = "insert into operator (operator_name, status) values (?, 'ACTIVE')";
	
	// it gets the status of the operator in the table
	private static final String GET_STATUS = "select status from operator where operator_id = ?";
	
	// it updates the status of operator.
	private static final String UPDATE_STATUS = "update operator set status = ? where operator_id = ?";
	
	public boolean operatorExists(String operatorName) {
		try {
			Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(CHECK_EXISTS);
            ps.setString(1, operatorName);
            
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to check operator existence", e);
		}
	}
	
	
	public int addOperator(String operatorName) {
		try {
			Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_OPERATOR, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, operatorName);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to add operator", e);
		}
	}
	
	
	public String getOperatorStatus(int operatorId) {
		try {
			Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_STATUS);
            ps.setInt(1, operatorId);

            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
            	throw new RuntimeException("Operator not found");
            }
            return rs.getString("status");
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch operator status", e);
		}
	}
	
	
	public void updateOperatorStatus(int operatorId, String status) {
		try {
			Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS);
            ps.setString(1, status);
            ps.setInt(2, operatorId);

            ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to update operator status", e);
		}
	}
	
	
	public List<String> findAllOperators(){
		List<String> operators = new ArrayList<>();
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ALL);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				String row = rs.getInt("operator_id") + " | " + rs.getString("operator_name") + " | " + rs.getString("status");
				operators.add(row);				
			}
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to fetch operator", e);
		}
		return operators;
	}
}
