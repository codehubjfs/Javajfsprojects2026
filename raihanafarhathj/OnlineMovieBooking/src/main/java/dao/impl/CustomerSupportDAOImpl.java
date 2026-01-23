package dao.impl;

import dao.CustomerSupportDAO;
import model.CustomerSupport;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import enums.SupportPriority;
import enums.SupportStatus;

public class CustomerSupportDAOImpl implements CustomerSupportDAO {

    @Override
    public int createTicket(CustomerSupport support) throws DataAccessException {

        String sql = """
            INSERT INTO customer_support (issue_type, description, priority, status, created_at, customer_id, assigned_admin)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, support.getIssueTitle());
            ps.setString(2, support.getIssueDescription());
            ps.setString(3, support.getPriority().name());
            ps.setString(4, support.getStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(support.getCreatedAt()));
            
            ps.setInt(6, support.getUserId());
            // assigned_admin can be null
            if (support.getAssignedAdmin() != null) {
                ps.setInt(7, support.getAssignedAdmin());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // returns generated supportId
                }
            }

            throw new DataAccessException("Failed to generate support ticket ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error creating support ticket", e);
        }
    }

    @Override
    public List<CustomerSupport> getTicketsByUser(int userId) throws DataAccessException {

        String sql = "SELECT * FROM customer_support WHERE customer_id = ? ORDER BY created_at DESC";
        List<CustomerSupport> tickets = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToSupport(rs));
                }
            }

            return tickets;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching tickets for user", e);
        }
    }

    @Override
    public boolean updateTicketStatus(int supportId, String status) throws DataAccessException {

        String sql = "UPDATE customer_support SET status = ? WHERE ticket_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, supportId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Error updating support ticket status", e);
        }
    }

    // ---------- Helper method ----------
    private CustomerSupport mapResultSetToSupport(ResultSet rs) throws SQLException {
        CustomerSupport support = new CustomerSupport();

        support.setIssueTitle(rs.getString("issue_type"));
        support.setIssueDescription(rs.getString("description"));
        support.setPriority(SupportPriority.valueOf(rs.getString("priority")));
        support.setStatus(SupportStatus.valueOf(rs.getString("status")));
        support.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        support.setUserId(rs.getInt("customer_id"));

        // set auto-increment ID using reflection
        try {
            java.lang.reflect.Field field = CustomerSupport.class.getDeclaredField("_supportId");
            field.setAccessible(true);
            field.set(support, rs.getInt("ticket_id"));
        } catch (Exception ignored) {}

        // Optional assigned admin
        int assignedAdmin = rs.getInt("assigned_admin");
        if (!rs.wasNull()) {
            try {
                java.lang.reflect.Field field = CustomerSupport.class.getDeclaredField("_assignedAdmin");
                field.setAccessible(true);
                field.set(support, assignedAdmin);
            } catch (Exception ignored) {}
        }

        return support;
    }
}
