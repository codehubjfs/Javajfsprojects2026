package dao.impl;

import dao.ReportDAO;
import model.Report;
import exception.DataAccessException;
import config.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import enums.ReportType;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public int generateReport(Report report) throws DataAccessException {

        String sql = """
            INSERT INTO report (report_type, description, generated_at, generated_by)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, report.getReportType().name());
            ps.setString(2, report.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(report.getGeneratedAt() != null 
                                                 ? report.getGeneratedAt() 
                                                 : LocalDateTime.now()));
            ps.setInt(4, report.getGeneratedBy());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);  // returns generated report_id
                }
            }

            throw new DataAccessException("Failed to generate report ID");

        } catch (SQLException e) {
            throw new DataAccessException("Error generating report", e);
        }
    }

    @Override
    public List<Report> getReportsByType(String type) throws DataAccessException {

        String sql = """
            SELECT * FROM report
            WHERE report_type = ?
            ORDER BY generated_at DESC
        """;

        List<Report> reports = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToReport(rs));
                }
            }

            return reports;

        } catch (SQLException e) {
            throw new DataAccessException("Error fetching reports by type", e);
        }
    }

    // ---------- Helper method ----------
    private Report mapResultSetToReport(ResultSet rs) throws SQLException {

        Report report = new Report(
                ReportType.valueOf(rs.getString("report_type")),
                rs.getString("description"),
                rs.getTimestamp("generated_at").toLocalDateTime(),
                rs.getInt("generated_by")
        );
        // set auto-increment ID internally
        try {
            java.lang.reflect.Field field = Report.class.getDeclaredField("_reportId");
            field.setAccessible(true);
            field.set(report, rs.getInt("report_id"));
        } catch (Exception ignored) {}
        return report;
    }
}
