package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.recharge.config.DBConnection;

public class OperatorDAO {

	// query used to get all operator
    private static final String GET_ALL =
            "select operator_id, operator_name, status from operator";

    // query used to check operator exits
    private static final String CHECK_EXISTS =
            "select count(*) from operator where operator_name = ?";

    // query used to insert new operator
    private static final String INSERT_OPERATOR =
            "insert into operator (operator_name, status) values (?, 'ACTIVE')";

    // query used to get operator status
    private static final String GET_STATUS_BY_ID =
            "select status from operator where operator_id = ?";

    // query used to get operatorId and status by name
    private static final String GET_BY_NAME =
            "select operator_id, status from operator where operator_name = ?";

    // query used to update status of the operator
    private static final String UPDATE_STATUS =
            "update operator set status = ? where operator_id = ?";

    /**
     * used to check operator exists by name
     * @param operatorName
     * @return
     */

    public boolean operatorExists(String operatorName) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(CHECK_EXISTS);
            ps.setString(1, operatorName);

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check operator existence", e);
        }
    }

    /**
     * used to add the operator by operator name
     * @param operatorName
     * @return
     */
    
    public int addOperator(String operatorName) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps =
                    conn.prepareStatement(INSERT_OPERATOR, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, operatorName);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add operator", e);
        }
    }

    /**
     * used to get operator status using operatorId
     * @param operatorId
     * @return
     */
    
    public String getOperatorStatus(int operatorId) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_STATUS_BY_ID);
            ps.setInt(1, operatorId);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Operator not found");
            }
            return rs.getString("status");
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch operator status", e);
        }
    }

    /**
     * used to update operator status
     * @param operatorId
     * @param status
     */
    
    public void updateOperatorStatus(int operatorId, String status) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS);
            ps.setString(1, status);
            ps.setInt(2, operatorId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update operator status", e);
        }
    }

    /**
     * used get operatorId by name
     * @param operatorName
     * @return
     */
    
    public int getOperatorIdByName(String operatorName) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_BY_NAME);
            ps.setString(1, operatorName);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Operator not found");
            }
            return rs.getInt("operator_id");
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve operator", e);
        }
    }
    
    /**
     * used to get operatorId by connectionId
     * @param connectionId
     * @return
     */
    
    public int getOperatorIdByConnection(int connectionId) {
    	try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                """
                select operator_id
                from mobile_connection
                where connection_id = ?
                """
            );

            ps.setInt(1, connectionId);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Operator not found for this connection");
            }

            return rs.getInt("operator_id");

        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve operator by connection", e);
        }
    }
    
    /**
     * used to get all available operators
     * @return
     */
    
    public List<String> findAllOperators() {
    	List<String> operators = new ArrayList<>();
    	try {
    		Connection conn = DBConnection.getConnection();
    		PreparedStatement ps = conn.prepareStatement(GET_ALL);
    		ResultSet rs = ps.executeQuery();
    		
    		while (rs.next()) {
    			operators.add(rs.getString("operator_name") + " | " + rs.getString("status"));
    		}
    	} catch (Exception e) {
    		throw new RuntimeException("Failed to fetch operators", e);
    	}
    	return operators;
    }
    
}
