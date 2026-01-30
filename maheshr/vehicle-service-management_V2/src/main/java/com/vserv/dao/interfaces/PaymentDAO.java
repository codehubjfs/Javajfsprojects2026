package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.Payment;

public interface PaymentDAO {
    List<Payment> findByInvoiceId(int invoiceId) throws SQLException;
    Payment insert(Payment payment) throws SQLException;
}