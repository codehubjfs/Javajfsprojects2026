package com.vserv.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vserv.config.DBConn;
import com.vserv.dao.interfaces.PaymentDAO;
import com.vserv.model.Payment;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public List<Payment> findByInvoiceId(int invoiceId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = """
            select * from payment 
            where invoice_id = ? 
            ORDER BY payment_id DESC
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSet(rs));
            }
        }
        return payments;
    }

    @Override
    public Payment insert(Payment payment) throws SQLException {
        String sql = """
            INSERT INTO payment (invoice_id, payment_method, transaction_reference, amount, payment_status)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DBConn.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getInvoiceId());
            stmt.setString(2, payment.getPaymentMethod());
            stmt.setString(3, payment.getTransactionReference());
            stmt.setBigDecimal(4, payment.getAmount());
            stmt.setString(5, payment.getPaymentStatus());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                payment.setPaymentId(rs.getInt(1));
            }
        }
        return payment;
    }

    private Payment mapResultSet(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setInvoiceId(rs.getInt("invoice_id"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setTransactionReference(rs.getString("transaction_reference"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        
        Timestamp paymentDate = rs.getTimestamp("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDateTime());
        }
        
        return payment;
    }
}