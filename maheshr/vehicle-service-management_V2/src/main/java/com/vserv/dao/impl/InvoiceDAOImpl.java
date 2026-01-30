package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.InvoiceDAO;
import com.vserv.model.Invoice;

public class InvoiceDAOImpl implements InvoiceDAO {

	@Override
	public List<Invoice> findAll() throws SQLException {
		List<Invoice> invoices = new ArrayList<>();
		String sql = """
				select i.*, u.full_name as customer_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				from invoice i
				JOIN service_record sr ON i.service_id = sr.service_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN user u ON v.user_id = u.user_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				ORDER BY i.invoice_id DESC
				""";

		try (Connection conn = DBConn.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				invoices.add(mapResultSet(rs));
			}
		}
		return invoices;
	}

	@Override
	public List<Invoice> findByCustomerId(int customerId) throws SQLException {
		List<Invoice> invoices = new ArrayList<>();
		String sql = """
				select i.*, u.full_name as customer_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				from invoice i
				JOIN service_record sr ON i.service_id = sr.service_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN user u ON v.user_id = u.user_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				where v.user_id = ?
				ORDER BY i.invoice_id DESC
				""";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, customerId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				invoices.add(mapResultSet(rs));
			}
		}
		return invoices;
	}

	@Override
	public Invoice findById(int invoiceId) throws SQLException {
		String sql = """
				select i.*, u.full_name as customer_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				from invoice i
				JOIN service_record sr ON i.service_id = sr.service_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN user u ON v.user_id = u.user_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				where i.invoice_id = ?
				""";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, invoiceId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return mapResultSet(rs);
			}
		}
		return null;
	}

	@Override
	public Invoice findByServiceId(int serviceId) throws SQLException {
		String sql = """
				select i.*, u.full_name as customer_name,
				       CONCAT(v.brand, ' ', v.model, ' (', v.registration_number, ')') as vehicle_info,
				       sc.service_name
				from invoice i
				JOIN service_record sr ON i.service_id = sr.service_id
				JOIN service_booking sb ON sr.booking_id = sb.booking_id
				JOIN vehicle v ON sb.vehicle_id = v.vehicle_id
				JOIN user u ON v.user_id = u.user_id
				JOIN service_catalog sc ON sb.catalog_id = sc.catalog_id
				where i.service_id = ?
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
	public Invoice insert(Invoice invoice) throws SQLException {
		String sql = """
				INSERT INTO invoice (service_id, total_amount, items_total, overtime_charge, payment_status)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (Connection conn = DBConn.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setInt(1, invoice.getServiceId());
			stmt.setBigDecimal(2, invoice.getTotalAmount());
			stmt.setBigDecimal(3, invoice.getItemsTotal());
			stmt.setBigDecimal(4, invoice.getOvertimeCharge());
			stmt.setString(5, invoice.getPaymentStatus());

			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				invoice.setInvoiceId(rs.getInt(1));
			}
		}
		return invoice;
	}

	@Override
	public void updatePaymentStatus(int invoiceId, String status) throws SQLException {
		String sql = "UPDATE invoice SET payment_status = ? where invoice_id = ?";

		try (Connection conn = DBConn.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, status);
			stmt.setInt(2, invoiceId);
			stmt.executeUpdate();
		}
	}

	private Invoice mapResultSet(ResultSet rs) throws SQLException {
		Invoice invoice = new Invoice();
		invoice.setInvoiceId(rs.getInt("invoice_id"));
		invoice.setServiceId(rs.getInt("service_id"));
		invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
		invoice.setItemsTotal(rs.getBigDecimal("items_total")); 
		invoice.setOvertimeCharge(rs.getBigDecimal("overtime_charge")); 
		invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
		invoice.setPaymentStatus(rs.getString("payment_status"));
		invoice.setCustomerName(rs.getString("customer_name"));
		invoice.setVehicleInfo(rs.getString("vehicle_info"));
		invoice.setServiceName(rs.getString("service_name"));
		return invoice;
	}
}