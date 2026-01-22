package com.dao;

import java.util.List;

import com.model.Invoice;

public interface InvoiceDAO {
	
	 int generateInvoice(Invoice invoice) throws Exception;

	    Invoice findById(int invoiceId) throws Exception;

	    Invoice findByOrderId(int orderId) throws Exception;

	    List<Invoice> findByUserId(int userId) throws Exception;
}
