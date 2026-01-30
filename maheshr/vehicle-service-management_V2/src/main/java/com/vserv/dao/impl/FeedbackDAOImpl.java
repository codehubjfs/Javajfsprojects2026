package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.FeedbackDAO;
import com.vserv.model.Feedback;

public class FeedbackDAOImpl implements FeedbackDAO {

    @Override
    public List<Feedback> findByCustomerId(int customerId) throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = """
            select sf.*, u.full_name as customer_name,
                   CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info
            from service_feedback sf
            JOIN user u ON sf.customer_id = u.user_id
            JOIN service_record sr ON sf.service_id = sr.service_id
            JOIN service_booking sb ON sr.booking_id = sb.booking_id
            JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
            where sf.customer_id = ?
            ORDER BY sf.submitted_at DESC
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                feedbacks.add(mapResultSet(rs));
            }
        }
        return feedbacks;
    }

    @Override
    public Feedback findByServiceId(int serviceId) throws SQLException {
        String sql = """
            select sf.*, u.full_name as customer_name,
                   CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info
            from service_feedback sf
            JOIN user u ON sf.customer_id = u.user_id
            JOIN service_record sr ON sf.service_id = sr.service_id
            JOIN service_booking sb ON sr.booking_id = sb.booking_id
            JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
            where sf.service_id = ?
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    @Override
    public Feedback insert(Feedback feedback) throws SQLException {
        String sql = """
            INSERT INTO service_feedback (service_id, customer_id, rating, feedback_text)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, feedback.getServiceId());
            stmt.setInt(2, feedback.getCustomerId());
            stmt.setInt(3, feedback.getRating());
            stmt.setString(4, feedback.getFeedbackText());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                feedback.setFeedbackId(rs.getInt(1));
            }
        }
        return feedback;
    }

    private Feedback mapResultSet(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getInt("feedback_id"));
        feedback.setServiceId(rs.getInt("service_id"));
        feedback.setCustomerId(rs.getInt("customer_id"));
        feedback.setRating(rs.getInt("rating"));
        feedback.setFeedbackText(rs.getString("feedback_text"));
        feedback.setCustomerName(rs.getString("customer_name"));
        feedback.setVehicleInfo(rs.getString("vehicle_info"));
        
        Timestamp submitted = rs.getTimestamp("submitted_at");
        if (submitted != null) {
            feedback.setSubmittedAt(submitted.toLocalDateTime());
        }
        
        return feedback;
    }
}