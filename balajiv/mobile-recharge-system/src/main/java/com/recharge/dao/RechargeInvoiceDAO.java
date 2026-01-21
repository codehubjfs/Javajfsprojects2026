package com.recharge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.recharge.config.DBConnection;
import com.recharge.model.RechargeInvoice;

public class RechargeInvoiceDAO {
	
	private static final String INSERT_INVOICE = 
			"""
			insert into recharge_invoice(recharge_id, generated_at, invoice_url)
			values(?, now(), ?)
			""";
	
	public void createInvoice(RechargeInvoice invoice) {
		
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_INVOICE);
			ps.setInt(1, invoice.getRechargeId());
			ps.setString(2,  invoice.getInvoiceUrl());
			
			ps.executeUpdate();
			
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to generate the invoice");
		}
	}
}
