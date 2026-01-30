package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.AdvisorDAO;
import com.vserv.model.Advisor;

public class AdvisorDAOImpl implements AdvisorDAO {

    @Override
    public List<Advisor> findAll() throws SQLException {
        List<Advisor> advisors = new ArrayList<>();
        String sql = """
            select sa.*, u.full_name, u.email
            from service_advisor sa
            join user u ON sa.advisor_id = u.user_id
            ORDER BY sa.current_load
            """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                advisors.add(mapResultSet(rs));
            }
        }
        return advisors;
    }

    @Override
    public List<Advisor> findAvailable() throws SQLException {
        List<Advisor> advisors = new ArrayList<>();
        String sql = """
            select sa.*, u.full_name, u.email
            from service_advisor sa
            join user u ON sa.advisor_id = u.user_id
            where sa.availability_status = 'AVAILABLE'
            ORDER BY sa.current_load, sa.last_assigned_at
            """;

        try (Connection conn = DBConn.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                advisors.add(mapResultSet(rs));
            }
        }
        return advisors;
    }

    @Override
    public Advisor findById(int advisorId) throws SQLException {
        String sql = """
            select sa.*, u.full_name, u.email
            from service_advisor sa
            join user u ON sa.advisor_id = u.user_id
            where sa.advisor_id = ?
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, advisorId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }
    
    @Override
    public void updateAdvisorStatus(int advisorId, String status) throws SQLException {
        String sql = "UPDATE service_advisor SET availability_status = ? WHERE advisor_id = ?";
        
        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, advisorId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateAvailability(int advisorId, String status) throws SQLException {
        String sql = "UPDATE service_advisor SET availability_status = ?, last_assigned_at = CURRENT_TIMESTAMP where advisor_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, advisorId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateLoad(int advisorId, int loadChange) throws SQLException {
        String sql = "UPDATE service_advisor SET current_load = current_load + ? where advisor_id = ?";

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loadChange);
            stmt.setInt(2, advisorId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void insert(Advisor advisor) throws SQLException {
        String sql = """
            INSERT INTO service_advisor (advisor_id, specialization, overtime_rate, availability_status)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, advisor.getAdvisorId());
            stmt.setString(2, advisor.getSpecialization());
            stmt.setBigDecimal(3, advisor.getOvertimeRate());
            stmt.setString(4, advisor.getAvailabilityStatus());
            stmt.executeUpdate();
        }
    }

    private Advisor mapResultSet(ResultSet rs) throws SQLException {
        Advisor advisor = new Advisor();
        advisor.setAdvisorId(rs.getInt("advisor_id"));
        advisor.setFullName(rs.getString("full_name"));
        advisor.setEmail(rs.getString("email"));
        advisor.setSpecialization(rs.getString("specialization"));
        advisor.setOvertimeRate(rs.getBigDecimal("overtime_rate"));
        advisor.setAvailabilityStatus(rs.getString("availability_status"));
        advisor.setCurrentLoad(rs.getInt("current_load"));
        
        Timestamp lastAssigned = rs.getTimestamp("last_assigned_at");
        if (lastAssigned != null) {
            advisor.setLastAssignedAt(lastAssigned.toLocalDateTime());
        }
        
        return advisor;
    }
}