package com.vserv.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import com.vserv.model.Invoice;

public interface InvoiceDAO {
    List<Invoice> findAll() throws SQLException;
    List<Invoice> findByCustomerId(int customerId) throws SQLException;
    Invoice findById(int invoiceId) throws SQLException;
    Invoice findByServiceId(int serviceId) throws SQLException;
    Invoice insert(Invoice invoice) throws SQLException;
    void updatePaymentStatus(int invoiceId, String status) throws SQLException;
}