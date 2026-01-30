package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.RecordDAO;
import com.vserv.model.ServiceRecord;

public class RecordDAOImpl implements RecordDAO {

	@Override
	public List<ServiceRecord> findByAdvisorId(int advisorId) throws SQLException {
		List<ServiceRecord> records = new ArrayList<>();
		String sql = """
				SELECT sr.*, u.full_name as advisor_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				FROM service_record sr
				JOIN user u ON sr.advisor_id = u.user_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				WHERE sr.advisor_id = ?
				ORDER BY sr.service_id DESC
				""";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, advisorId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				records.add(mapResultSet(rs));
			}
		}
		return records;
	}

	@Override
	public List<ServiceRecord> findByStatus(String status) throws SQLException {
		List<ServiceRecord> records = new ArrayList<>();
		String sql = """
				SELECT sr.*, u.full_name as advisor_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				FROM service_record sr
				JOIN user u ON sr.advisor_id = u.user_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				WHERE sr.status = ?
				ORDER BY sr.service_id DESC
				""";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, status);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				records.add(mapResultSet(rs));
			}
		}
		return records;
	}

	@Override
	public ServiceRecord findById(int serviceId) throws SQLException {
		String sql = """
				SELECT sr.*, u.full_name as advisor_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				FROM service_record sr
				JOIN user u ON sr.advisor_id = u.user_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				WHERE sr.service_id = ?
				""";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, serviceId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return mapResultSet(rs);
			}
		}
		return null;
	}

	@Override
	public ServiceRecord findByBookingId(int bookingId) throws SQLException {
		String sql = """
				SELECT sr.*, u.full_name as advisor_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				FROM service_record sr
				JOIN user u ON sr.advisor_id = u.user_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				WHERE sr.booking_id = ?
				""";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, bookingId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return mapResultSet(rs);
			}
		}
		return null;
	}

	@Override
	public ServiceRecord insert(ServiceRecord record) throws SQLException {
	    String sql = """
	        INSERT INTO service_record (booking_id, advisor_id, status, remarks, estimated_hours)
	        VALUES (?, ?, ?, ?, ?)
	        """;

	    try (Connection conn = DBConn.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	        stmt.setInt(1, record.getBookingId());
	        stmt.setInt(2, record.getAdvisorId());
	        stmt.setString(3, record.getStatus());
	        stmt.setString(4, record.getRemarks());
	        
	        if (record.getEstimatedHours() != null) {
	            stmt.setDouble(5, record.getEstimatedHours());
	        } else {
	            stmt.setNull(5, Types.DOUBLE);
	        }

	        stmt.executeUpdate();

	        ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            record.setServiceId(rs.getInt(1));
	        }
	    }
	    return record;
	}

	@Override
	public void updateStatus(int serviceId, String status) throws SQLException {
		String sql = "UPDATE service_record SET status = ? WHERE service_id = ?";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, status);
			stmt.setInt(2, serviceId);
			stmt.executeUpdate();
		}
	}

	@Override
	public void updateRemarks(int serviceId, String remarks) throws SQLException {
		String sql = "UPDATE service_record SET remarks = ? WHERE service_id = ?";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, remarks);
			stmt.setInt(2, serviceId);
			stmt.executeUpdate();
		}
	}

	@Override
	public void complete(int serviceId) throws SQLException {
		String sql = """
				UPDATE service_record
				SET status = 'COMPLETED', service_end_date = CURRENT_TIMESTAMP
				WHERE service_id = ?
				""";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, serviceId);
			stmt.executeUpdate();
		}
	}

	@Override
	public void updateActualHours(int serviceId, double actualHours) throws SQLException {
		String sql = "UPDATE service_record SET actual_hours = ? WHERE service_id = ?";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setDouble(1, actualHours);
			stmt.setInt(2, serviceId);
			stmt.executeUpdate();
		}
	}

	private ServiceRecord mapResultSet(ResultSet rs) throws SQLException {
		ServiceRecord record = new ServiceRecord();
		record.setServiceId(rs.getInt("service_id"));
		record.setBookingId(rs.getInt("booking_id"));
		record.setAdvisorId(rs.getInt("advisor_id"));
		record.setStatus(rs.getString("status"));
		record.setRemarks(rs.getString("remarks"));
		record.setAdvisorName(rs.getString("advisor_name"));
		record.setVehicleInfo(rs.getString("vehicle_info"));
		record.setServiceName(rs.getString("service_name"));

		Double estimatedHours = rs.getDouble("estimated_hours");
		if (!rs.wasNull()) {
			record.setEstimatedHours(estimatedHours);
		}

		Double actualHours = rs.getDouble("actual_hours");
		if (!rs.wasNull()) {
			record.setActualHours(actualHours);
		}

		Timestamp startDate = rs.getTimestamp("service_start_date");
		if (startDate != null) {
			record.setServiceStartDate(startDate.toLocalDateTime());
		}

		Timestamp endDate = rs.getTimestamp("service_end_date");
		if (endDate != null) {
			record.setServiceEndDate(endDate.toLocalDateTime());
		}

		return record;
	}
}